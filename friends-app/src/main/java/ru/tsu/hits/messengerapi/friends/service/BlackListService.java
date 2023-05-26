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
import ru.tsu.hits.messengerapi.friends.dto.BlackListPageDto;
import ru.tsu.hits.messengerapi.friends.dto.BlockedUserDto;
import ru.tsu.hits.messengerapi.friends.dto.SearchBlockedUserDto;
import ru.tsu.hits.messengerapi.friends.entity.BlockedUserEntity;
import ru.tsu.hits.messengerapi.friends.entity.FriendEntity;
import ru.tsu.hits.messengerapi.friends.mapper.BlackListMapper;
import ru.tsu.hits.messengerapi.friends.repository.BlackListRepository;
import ru.tsu.hits.messengerapi.friends.repository.FriendRepository;
import ru.tsu.hits.messengerapi.friends.util.ErrorConstant;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для работы с черным списком.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BlackListService {

    private final BlackListRepository blackListRepository;
    private final BlackListMapper blackListMapper;
    private final ConvertDateService convertDateService;
    private final FriendRepository friendRepository;
    private final IntegrationRequestService integrationRequestService;
    private final StreamBridge streamBridge;

    /**
     * Метод для добавления пользователя в черный список.
     *
     * @param externalUserId идентификатор пользователя, которого нужно заблокировать.
     * @param targetUserId      id целевого пользователя.
     * @return {@link BlockedUserDto} дто с информацией о пользователе в блэклисте.
     */
    @Transactional
    public BlockedUserDto addBlockedUser(UUID externalUserId, UUID targetUserId) {

        String fullName = integrationRequestService.checkId(externalUserId).getFullName();
        checkBlockedUser(externalUserId, targetUserId);
        Optional<BlockedUserEntity> blockedUserEntity = isAdded(externalUserId, targetUserId);
        if (blockedUserEntity.isEmpty()) {
            BlockedUserEntity newblockedUserEntity = blackListMapper.addBlockedUserDtoToBlockedUserEntity(
                    externalUserId,
                    fullName,
                    targetUserId);
            blackListRepository.save(newblockedUserEntity);
            deleteFromFriends(newblockedUserEntity);
            sendAddedToBlackListNotification(targetUserId, externalUserId);
            return blackListMapper.blockedUserEntityToBlockedUserDto(newblockedUserEntity);
        } else {
            return blackListMapper.blockedUserEntityToBlockedUserDto(updateRemovalDate(
                    blockedUserEntity.get(),
                    fullName)
            );
        }
    }

    /**
     * Метод для получения информации о заблокированном пользователе.
     *
     * @param blockedUserId id пользователя, которого заблокировали.
     * @param targetUserId  id целевого пользователя.
     * @return {@link BlockedUserDto} дто с информацией о пользователе в блэклисте.
     */
    @Transactional(readOnly = true)
    public BlockedUserDto getBlockedUser(UUID blockedUserId, UUID targetUserId) {
        BlockedUserEntity blockedUserEntity = findBlockedUserEntity(targetUserId, blockedUserId);
        return blackListMapper.blockedUserEntityToBlockedUserDto(blockedUserEntity);
    }

    /**
     * Метод для удаления пользователя из черного списка.
     *
     * @param blockedUserId id пользователя, которого заблокировали.
     * @param targetUserId  id целевого пользователя.
     */
    @Transactional
    public void deleteBlockedUser(UUID blockedUserId, UUID targetUserId) {
        BlockedUserEntity blockedUserEntity = findBlockedUserEntity(targetUserId, blockedUserId);
        if (blockedUserEntity.getBlockedUserRemovalDate() != null) {
            log.error(ErrorConstant.ALREADY_REMOVED_FROM_BLACKLIST);
            throw new BadRequestException(ErrorConstant.ALREADY_REMOVED_FROM_BLACKLIST);
        }
        blockedUserEntity.setBlockedUserRemovalDate(convertDateService.convertToDate(LocalDate.now()));
        blackListRepository.save(blockedUserEntity);
        sendRemovedFromBlackListNotification(targetUserId, blockedUserId);
    }

    /**
     * Метод для получения информации заблокирован ли внешний пользователь у текущего пользователя.
     *
     * @param blockedUserId id пользователя, которого заблокировали.
     * @param targetUserId  id целевого пользователя.
     */
    @Transactional(readOnly = true)
    public boolean isBlockedUser(UUID blockedUserId, UUID targetUserId) {
        integrationRequestService.checkId(blockedUserId);
        return blackListRepository.existsByExternalUserAndTargetUserAndBlockedUserRemovalDate(targetUserId, blockedUserId, null);
    }

    /**
     * Возвращает страницу объектов DTO друзей целевого пользователя согласно переданным параметрам страницы.
     *
     * @param blackListPageDto объект DTO, содержащий информацию о запрашиваемой странице (номер страницы и количество элементов на странице)
     * @param targetUserId     идентификатор целевого пользователя
     * @return страницу объектов DTO друзей целевого пользователя
     */
    @Transactional(readOnly = true)
    public Page<BlockedUserDto> getBlackListPage(BlackListPageDto blackListPageDto, UUID targetUserId) {

        Pageable pageable = PageRequest.of(
                blackListPageDto.getPageableDto().getPageNumber(),
                blackListPageDto.getPageableDto().getPageSize()
        );

        String wildcardFilter = "%" + blackListPageDto.getFullNameFilter() + "%";
        return blackListRepository
                .findByTargetUserAndFullNameLikeAndBlockedUserRemovalDate
                        (targetUserId,
                                wildcardFilter,
                                null,
                                pageable)
                .map(blackListMapper::blockedUserEntityToBlockedUserDto);
    }

    /**
     * Метод для поиска сущности пользователя в черном списке по идентификаторам целевого и внешнего пользователей.
     *
     * @param targetUserId   Идентификатор целевого пользователя.
     * @param externalUserId Идентификатор внешнего пользователя.
     * @return Сущность пользователя в черном списке.
     * @throws NotFoundException Если пользователь не найден в черном списке.
     */
    private BlockedUserEntity findBlockedUserEntity(UUID targetUserId, UUID externalUserId) {
        return blackListRepository
                .findByTargetUserAndExternalUser(targetUserId, externalUserId)
                .orElseThrow(() -> {
                    log.error(ErrorConstant.NOT_FOUND_IN_BLACKLIST);
                    throw new NotFoundException(ErrorConstant.NOT_FOUND_IN_BLACKLIST);
                });
    }

    /**
     * Метод для проверки, добавлен ли пользователь в черный список.
     *
     * @param externalUserId Идентификатор внешнего пользователя.
     * @param targetUserId   Идентификатор целевого пользователя.
     * @return Optional объект с сущностью пользователя в черном списке, если пользователь добавлен, иначе empty.
     */
    private Optional<BlockedUserEntity> isAdded(UUID externalUserId, UUID targetUserId) {
        return blackListRepository
                .findByTargetUserAndExternalUser(targetUserId, externalUserId);
    }

    /**
     * Метод для обновления даты удаления пользователя из черного списка.
     *
     * @param blockedUserEntity Сущность пользователя в черном списке.
     * @return Сущность пользователя в черном списке с обновленной датой удаления.
     * @throws BadRequestException Если пользователь уже добавлен в черный список.
     */
    private BlockedUserEntity updateRemovalDate(BlockedUserEntity blockedUserEntity, String fullName) {
        if (blockedUserEntity.getBlockedUserRemovalDate() == null) {
            throw new BadRequestException(ErrorConstant.ALREADY_ADDED_TO_BLACKLIST);
        } else {
            blockedUserEntity.setBlockedUserRemovalDate(null);
            blockedUserEntity.setFullName(fullName);
            blackListRepository.save(blockedUserEntity);
            deleteFromFriends(blockedUserEntity);
            sendAddedToBlackListNotification(blockedUserEntity.getTargetUser(), blockedUserEntity.getExternalUser());
            return blockedUserEntity;
        }
    }

    /**
     * Метод для поиска пользователей в черном списке с возможностью фильтрации и пагинацией.
     *
     * @param searchBlockedUserDto DTO объект с параметрами поиска и пагинации.
     * @param targetUserId         Идентификатор целевого пользователя.
     * @return Страница объектов DTO с пользователями в черном списке.
     * @throws BadRequestException Если передан некорректный критерий сортировки.
     */
    @Transactional(readOnly = true)
    public Page<BlockedUserDto> searchBlockedUser(SearchBlockedUserDto searchBlockedUserDto, UUID targetUserId) {
        try {
            Pageable pageable = createPageable(searchBlockedUserDto);

            if (searchBlockedUserDto.getFilters() != null) {
                Example<BlockedUserEntity> example = Example.of(
                        BlockedUserEntity.from(
                                searchBlockedUserDto.getFilters().getFullName(),
                                searchBlockedUserDto.getFilters().getExternalUser(),
                                searchBlockedUserDto.getFilters().getAddedDate(),
                                searchBlockedUserDto.getFilters().getRemovalDate(),
                                targetUserId
                        ),
                        createExampleMatcher()
                );
                return blackListRepository.findAll(example, pageable)
                        .map(blackListMapper::blockedUserEntityToBlockedUserDto);
            } else {
                return blackListRepository.findByTargetUser(targetUserId, pageable)
                        .map(blackListMapper::blockedUserEntityToBlockedUserDto);
            }
        } catch (PropertyReferenceException propertyReferenceException) {
            throw new BadRequestException(String.format("Некорректный критерий сортировки %s",
                    propertyReferenceException.getPropertyName())
            );
        }
    }

    /**
     * Метод для создания объекта ExampleMatcher для использования в поиске сущностей черного списка.
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
     * Метод для создания объекта Pageable для использования в поиске сущностей черного списка.
     *
     * @param searchBlockedUserDto DTO объект с параметрами поиска и пагинации.
     * @return Объект Pageable.
     */
    private Pageable createPageable(SearchBlockedUserDto searchBlockedUserDto) {
        int page = searchBlockedUserDto.getPageableDto().getPageNumber();
        int size = searchBlockedUserDto.getPageableDto().getPageSize();
        return PageRequest.of(page, size);
    }

    /**
     *  Удаляет {@link BlockedUserEntity} из списка друзей как целевого пользователя, так и внешнего пользователя.
     *
     *  @param blockedUserEntity сущность {@link BlockedUserEntity}, которую нужно удалить из списка друзей
     */
    @Transactional
    public void deleteFromFriends(BlockedUserEntity blockedUserEntity) {
        setRemovalDate(friendRepository.findByTargetUserAndExternalUser
                (blockedUserEntity.getTargetUser(),
                        blockedUserEntity.getExternalUser()
                ));
        setRemovalDate(friendRepository.findByTargetUserAndExternalUser
                (blockedUserEntity.getExternalUser(),
                        blockedUserEntity.getTargetUser()
                ));
    }

    /**
     *   Устанавливает дату удаления друга на текущую дату для {@link FriendEntity}.
     *
     *   @param friend {@link Optional<FriendEntity>}, для которой нужно установить дату удаления друга
     */
    private void setRemovalDate(Optional<FriendEntity> friend) {
        friend.ifPresent(friendEntity ->
                friendEntity.setFriendRemovalDate(
                        convertDateService.convertToDate(LocalDate.now())
                )
        );
    }

    /**
     * Проверяет, является ли целевой пользователь и внешний пользователь одним и тем же пользователем.
     *
     * @param externalUserId идентификатор внешнего пользователя
     * @param targetUserId   идентификатор целевого пользователя
     * @throws ConflictException если целевой пользователь и внешний пользователь имеют одинаковый идентификатор
     */
    private void checkBlockedUser(UUID externalUserId, UUID targetUserId){
        if (targetUserId.equals(externalUserId)) {
            throw new ConflictException(ErrorConstant.ADDING_YOURSELF_TO_THE_BLACKLIST);
        }
    }


    /**
     * Отправляет уведомление о добавлении пользователя в черный список.
     *
     * @param targetUserId идентификатор пользователя, который добавил в черный список
     * @param externalUserId внешний идентификатор пользователя, которому отправляется уведомление
     */
    private void sendAddedToBlackListNotification(UUID targetUserId, UUID externalUserId) {

        String fullName = integrationRequestService.getFullName(targetUserId).getFullName();
        NewNotificationDto newNotificationDto = new NewNotificationDto(externalUserId,
                NotificationType.ADDED_TO_BLACK_LIST,
                "Пользователь " + fullName + " добавил Вас в черный список."
        );
        streamBridge.send(RabbitMQBindings.CREATE_NOTIFICATION_OUT, newNotificationDto);
    }

    /**
     * Отправляет уведомление об удалении пользователя из черного списка.
     *
     * @param targetUserId идентификатор пользователя, который удалил из черного списка
     * @param externalUserId внешний идентификатор пользователя, которому отправляется уведомление
     */
    private void sendRemovedFromBlackListNotification(UUID targetUserId, UUID externalUserId) {

        String fullName = integrationRequestService.getFullName(targetUserId).getFullName();
        NewNotificationDto newNotificationDto = new NewNotificationDto(externalUserId,
                NotificationType.REMOVED_FROM_BLACK_LIST,
                "Пользователь " + fullName + " удалил Вас из черного списка."
        );
        streamBridge.send(RabbitMQBindings.CREATE_NOTIFICATION_OUT, newNotificationDto);
    }

}