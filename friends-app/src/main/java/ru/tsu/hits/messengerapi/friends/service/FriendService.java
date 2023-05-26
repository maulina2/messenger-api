package ru.tsu.hits.messengerapi.friends.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.*;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsu.hits.messengerapi.common.dto.NewNotificationDto;
import ru.tsu.hits.messengerapi.common.enumeration.NotificationType;
import ru.tsu.hits.messengerapi.common.exception.BadRequestException;
import ru.tsu.hits.messengerapi.common.exception.ConflictException;
import ru.tsu.hits.messengerapi.common.exception.NotFoundException;
import ru.tsu.hits.messengerapi.common.service.ConvertDateService;
import ru.tsu.hits.messengerapi.common.util.RabbitMQBindings;
import ru.tsu.hits.messengerapi.commonsecurity.dto.JwtUserData;
import ru.tsu.hits.messengerapi.friends.dto.FriendDto;
import ru.tsu.hits.messengerapi.friends.dto.FriendPageDto;
import ru.tsu.hits.messengerapi.friends.dto.SearchFriendDto;
import ru.tsu.hits.messengerapi.friends.entity.FriendEntity;
import ru.tsu.hits.messengerapi.friends.mapper.FriendMapper;
import ru.tsu.hits.messengerapi.friends.repository.BlackListRepository;
import ru.tsu.hits.messengerapi.friends.repository.FriendRepository;
import ru.tsu.hits.messengerapi.friends.util.ErrorConstant;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для обработки запросов, связанных с друзьями у текущего пользователя.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendMapper friendMapper;
    private final FriendRepository friendRepository;
    private final BlackListRepository blackListRepository;
    private final ConvertDateService convertDateService;
    private final IntegrationRequestService integrationRequestService;
    private final StreamBridge streamBridge;

    /**
     * Добавляет друзей текущего пользователя, основываясь на полученных данных.
     *
     * @param externalUserId идентификатор пользователя, которого нужно добавить в друзья
     * @param targetUserData объект, содержащий информацию о целевом пользователе, которого текущий пользователь хочет добавить в друзья.
     * @return объект FriendDto, содержащий информацию о добавленном друге, включая его идентификатор, имя, фамилию и т.д.
     */
    @Transactional
    public FriendDto addFriend(UUID externalUserId, JwtUserData targetUserData) {

        String fullName = integrationRequestService.checkId(externalUserId).getFullName();
        checkBlackList(targetUserData.getId(), externalUserId); //проверка нет ли в черном списке
        checkFriend(externalUserId, targetUserData.getId()); //проверка на самого себя

        Optional<FriendEntity> friendEntity = isAdded(externalUserId, targetUserData.getId());
        Optional<FriendEntity> mutualFriendEntity = isAdded(targetUserData.getId(), externalUserId);

        if (friendEntity.isEmpty()) {
            log.info("Пользователь еще не был ранее добавлен в друзья");
            createFriend(targetUserData, externalUserId);
            sendAddedToFriendListNotification(targetUserData.getId(), externalUserId );
            return friendMapper.friendEntityToFriendDto(createFriend(
                    externalUserId,
                    fullName,
                    targetUserData.getId()
                    )
            );
        } else {
            log.info("До этого удаляли из друзей");
            updateRemovalDate(mutualFriendEntity.get(), targetUserData.getFullName());
            return friendMapper.friendEntityToFriendDto(updateRemovalDate(friendEntity.get(), fullName));
        }
    }

    /**
     * Возвращает объект DTO друга по идентификатору друга и текущего пользователя.
     *
     * @param friendId     идентификатор друга
     * @param targetUserId идентификатор целевого пользователя
     * @return объект DTO друга
     */
    @Transactional(readOnly = true)
    public FriendDto getFriend(UUID friendId, UUID targetUserId) {

        FriendEntity friendEntity = findFriendEntity(targetUserId, friendId);
        return friendMapper.friendEntityToFriendDto(friendEntity);
    }

    /**
     * Возвращает страницу объектов DTO друзей целевого пользователя согласно переданным параметрам страницы.
     *
     * @param friendPageDto объект DTO, содержащий информацию о запрашиваемой странице (номер страницы и количество элементов на странице)
     * @param targetUserId  идентификатор целевого пользователя
     * @return страницу объектов DTO друзей целевого пользователя
     */
    @Transactional(readOnly = true)
    public Page<FriendDto> getFriendPage(FriendPageDto friendPageDto, UUID targetUserId) {

        Pageable pageable = PageRequest.of(
                friendPageDto.getPageableDto().getPageNumber(),
                friendPageDto.getPageableDto().getPageSize()
        );

        String wildcardFilter = "%" + friendPageDto.getFullNameFilter() + "%";
        return friendRepository
                .findByTargetUserAndFullNameLikeAndFriendRemovalDate(targetUserId, wildcardFilter, null, pageable)
                .map(friendMapper::friendEntityToFriendDto);
    }

    /**
     * Метод для взаимного удаления из друзей.
     *
     * @param friendId     id внешнего пользователя.
     * @param targetUserId id целевого пользователя.
     */
    @Transactional
    public void deleteFriends(UUID friendId, UUID targetUserId) {
        removeFriend(friendId, targetUserId);
        removeFriend(targetUserId, friendId);
    }

    /**
     * Метод для поиска друзей пользователя с возможностью фильтрации и пагинацией.
     *
     * @param searchFriendDto DTO объект с параметрами поиска и пагинации.
     * @param targetUserId    Идентификатор целевого пользователя.
     * @return Страница объектов DTO с друзьями пользователя.
     * @throws BadRequestException Если передан некорректный критерий сортировки.
     */
    @Transactional(readOnly = true)
    public Page<FriendDto> searchFriend(SearchFriendDto searchFriendDto, UUID targetUserId) {
        try {
            Pageable pageable = createPageable(searchFriendDto);

            if (searchFriendDto.getFilters() != null) {
                Example<FriendEntity> example = Example.of(
                        FriendEntity.from(
                                searchFriendDto.getFilters().getFullName(),
                                searchFriendDto.getFilters().getExternalUser(),
                                searchFriendDto.getFilters().getAddedDate(),
                                searchFriendDto.getFilters().getRemovalDate(),
                                targetUserId
                        ),
                        createExampleMatcher()
                );
                return friendRepository.findAll(example, pageable)
                        .map(friendMapper::friendEntityToFriendDto);
            } else {
                return friendRepository.findByTargetUser(targetUserId, pageable)
                        .map(friendMapper::friendEntityToFriendDto);
            }
        } catch (PropertyReferenceException propertyReferenceException) {
            throw new BadRequestException(String.format("Некорректный критерий сортировки %s",
                    propertyReferenceException.getPropertyName())
            );
        }
    }

    /**
     * Метод для создания объекта ExampleMatcher для использования в поиске сущностей друзей.
     *
     * @return Объект ExampleMatcher.
     */
    private ExampleMatcher createExampleMatcher() {
        return ExampleMatcher
                .matchingAll()
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    }

    /**
     * Метод для создания объекта Pageable для использования в поиске сущностей друзей.
     *
     * @param searchFriendDto DTO объект с параметрами поиска и пагинации.
     * @return Объект Pageable.
     */
    private Pageable createPageable(SearchFriendDto searchFriendDto) {
        int page = searchFriendDto.getPageableDto().getPageNumber();
        int size = searchFriendDto.getPageableDto().getPageSize();
        return PageRequest.of(page, size);
    }

    /**
     * Метод для поиска сущности друга по идентификаторам целевого и внешнего пользователей.
     *
     * @param targetUserId   Идентификатор целевого пользователя.
     * @param externalUserId Идентификатор внешнего пользователя.
     * @return Сущность друга.
     * @throws NotFoundException Если друг не найден.
     */
    private FriendEntity findFriendEntity(UUID targetUserId, UUID externalUserId) {
        return friendRepository
                .findByTargetUserAndExternalUser(targetUserId, externalUserId)
                .orElseThrow(() -> {
                    throw new NotFoundException(ErrorConstant.NOT_FOUND_IN_FRIENDS);
                });
    }

    /**
     * Метод для проверки, добавлен ли пользователь в друзья.
     *
     * @param externalUserId Идентификатор внешнего пользователя.
     * @param targetUserId   Идентификатор целевого пользователя.
     * @return Optional объект с сущностью друга, если пользователь добавлен, иначе empty.
     */
    private Optional<FriendEntity> isAdded(UUID externalUserId, UUID targetUserId) {
        return friendRepository
                .findByTargetUserAndExternalUser(targetUserId, externalUserId);
    }

    /**
     * Метод для обновления даты удаления пользователя из списка друзей.
     *
     * @param friendEntity Сущность друга.
     * @return Сущность друга с обновленной датой удаления.
     * @throws BadRequestException Если пользователь уже добавлен в список друзей.
     */
    @Transactional
    public FriendEntity updateRemovalDate(FriendEntity friendEntity, String fullName) {
        if (friendEntity.getFriendRemovalDate() == null) {
            throw new BadRequestException(ErrorConstant.ALREADY_ADDED_TO_FRIENDS);
        } else {
            sendAddedToFriendListNotification(friendEntity.getTargetUser(), friendEntity.getExternalUser());
            friendEntity.setFriendRemovalDate(null);
            friendEntity.setFullName(fullName);
            friendRepository.save(friendEntity);
            return friendEntity;
        }
    }

    /**
     * Проверка на то, находится ли кто-то из пользователей у другого в черном списке
     *
     * @param targetUserId   идентификатор целевого пользователя
     * @param externalUserId идентификатор внешнего пользователя
     * @throws ConflictException если целевой пользователь находится в черном списке внешнего пользователя
     */
    private void checkBlackList(UUID targetUserId, UUID externalUserId) {
        if (blackListRepository.findByTargetUserAndExternalUserAndBlockedUserRemovalDate(
                targetUserId, externalUserId, null).isPresent()
        ) {
            throw new ConflictException("Вы не можете добавить пользователя в друзья," +
                    " пока он находится у вас в черном списке");
        }
        if (blackListRepository.findByTargetUserAndExternalUserAndBlockedUserRemovalDate(
                externalUserId, targetUserId, null).isPresent()
        ) {
            throw new ConflictException("Вы не можете добавить пользователя в друзья," +
                    " пока вы находитесь у него в черном списке");
        }
    }

    /**
     * Проверяет, является ли целевой пользователь и внешний пользователь одним и тем же пользователем.
     *
     * @param externalUserId идентификатор внешнего пользователя
     * @param targetUserId   идентификатор целевого пользователя
     * @throws ConflictException если целевой пользователь и внешний пользователь имеют одинаковый идентификатор
     */
    private void checkFriend(UUID externalUserId, UUID targetUserId) {
        if (targetUserId.equals(externalUserId)) {
            throw new ConflictException(ErrorConstant.ADDING_YOURSELF_TO_THE_FRIENDS);
        }
    }

    /**
     * Удаляет друга по переданным идентификаторам. Если друг уже был удален ранее, выбрасывает исключение BadRequestException.
     *
     * @param friendId     идентификатор друга, которого нужно удалить
     * @param targetUserId идентификатор целевого пользователя
     * @throws BadRequestException если друг уже был удален ранее
     */
    @Transactional
    public void removeFriend(UUID friendId, UUID targetUserId) {
        FriendEntity friendEntity = findFriendEntity(targetUserId, friendId);
        if (friendEntity.getFriendRemovalDate() == null) {
            friendEntity.setFriendRemovalDate(convertDateService.convertToDate(LocalDate.now()));
            friendRepository.save(friendEntity);
            log.info("Удалили из друзей");
            sendRemovedFromFriendListNotification(friendEntity.getTargetUser(), friendEntity.getExternalUser());
        } else {
            throw new BadRequestException(ErrorConstant.ALREADY_REMOVED_FROM_FRIENDS);
        }
    }

    /**
     * Метод для создания сущности взаимного друга.
     * @param targetUserData информация о целевом пользователе.
     * @param externalUserId id внешнего пользователя.
     */
    @Transactional
    public void createFriend(JwtUserData targetUserData, UUID externalUserId) {
        FriendEntity mutualFriendEntity = friendMapper.JwtUserDataToFriendEntity(targetUserData, externalUserId);
        friendRepository.save(mutualFriendEntity);
        log.info("Создали сущность взаимного друга");
    }

    /**
     * Метод для создания сущности друга.
     *
     * @param externalUserId идентификатор пользователя, которого нужно добавить в друзья
     * @param userId id целевого пользователя
     */
    @Transactional
    public FriendEntity createFriend(UUID externalUserId, String fullName, UUID userId) {
        FriendEntity newFriendEntity = friendMapper.addFriendDtoToFriendEntity(externalUserId, fullName, userId);
        friendRepository.save(newFriendEntity);
        return newFriendEntity;
    }

    /**
     * Отправляет уведомление о добавлении пользователя в список друзей.
     *
     * @param targetUserId идентификатор пользователя, который добавил в друзья
     * @param externalUserId внешний идентификатор пользователя, которому отправляется уведомление
     */
    private void sendAddedToFriendListNotification(UUID targetUserId, UUID externalUserId) {

        String fullName = integrationRequestService.getFullName(targetUserId).getFullName();
        NewNotificationDto newNotificationDto = new NewNotificationDto(externalUserId,
                NotificationType.ADDED_TO_FRIENDS,
                "Пользователь " + fullName + " добавил Вас в друзья."
        );
        streamBridge.send(RabbitMQBindings.CREATE_NOTIFICATION_OUT, newNotificationDto);
    }

    /**
     * Отправляет уведомление об удалении пользователя из списка друзей.
     *
     * @param targetUserId идентификатор пользователя, который удалил из друзей
     * @param externalUserId внешний идентификатор пользователя, которому отправляется уведомление
     */
    private void sendRemovedFromFriendListNotification(UUID targetUserId, UUID externalUserId) {

        String fullName = integrationRequestService.getFullName(targetUserId).getFullName();
        NewNotificationDto newNotificationDto = new NewNotificationDto(externalUserId,
                NotificationType.REMOVED_FROM_FRIENDS,
                "Пользователь " + fullName + " удалил Вас из друзей."
        );
        streamBridge.send(RabbitMQBindings.CREATE_NOTIFICATION_OUT, newNotificationDto);
    }

}
