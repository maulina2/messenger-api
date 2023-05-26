package ru.tsu.hits.messengerapi.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsu.hits.messengerapi.common.dto.UserDto;
import ru.tsu.hits.messengerapi.common.exception.BadRequestException;
import ru.tsu.hits.messengerapi.user.dto.UserPageDto;
import ru.tsu.hits.messengerapi.user.entity.UserEntity;
import ru.tsu.hits.messengerapi.user.mapper.UserMapper;
import ru.tsu.hits.messengerapi.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Сервис для получения информации о пользователях.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final IntegrationRequestService integrationRequestService;
    private final FindUserService findUserService;

    @Transactional(readOnly = true)
    public UserDto getExternalUserInfo(UUID id, UUID targetUserId) {
        integrationRequestService.isBlockedUser(id, targetUserId);
        UserEntity user = findUserService.getUserById(id);
        return userMapper.userToUserDto(user);
    }

    /**
     * Метод для получения страницы пользователей с возможностью фильтрации и сортировки.
     *
     * @param userPageDto объект типа UserPageDto, содержащий параметры страницы пользователей.
     * @return страницу объектов типа UserDto, содержащую информацию о пользователях.
     * @throws org.springframework.data.mapping.PropertyReferenceException если указан некорректный критерий сортировки.
     * @throws BadRequestException                                         если указаны некорректные параметры пагинации.
     */
    @Transactional(readOnly = true)
    public Page<UserDto> getUsersPage(UserPageDto userPageDto) {
        try {
            Pageable pageable = createPageable(userPageDto);

            if (userPageDto.getFilters() != null) {
                Example<UserEntity> example = Example.of(
                        UserEntity.from(
                                userPageDto.getFilters().getFullNameFilter(),
                                userPageDto.getFilters().getCityFilter(),
                                userPageDto.getFilters().getEmailFilter(),
                                userPageDto.getFilters().getLoginFilter(),
                                userPageDto.getFilters().getPhoneNumberFilter(),
                                userPageDto.getFilters().getBirthDateFilter()),
                        createExampleMatcher()
                );

                return userRepository.findAll(example, pageable).map(userMapper::userToUserDto);
            } else {
                return userRepository.findAll(pageable).map(userMapper::userToUserDto);
            }
        } catch (PropertyReferenceException propertyReferenceException) {
            throw new BadRequestException(String.format("Некорректный критерий сортировки %s",
                    propertyReferenceException.getPropertyName())
            );
        }
    }

    /**
     * Метод для создания объекта ExampleMatcher, используемого для фильтрации списка пользователей.
     *
     * @return объект типа ExampleMatcher, используемый для фильтрации списка пользователей.
     */
    private ExampleMatcher createExampleMatcher() {
        return ExampleMatcher
                .matchingAll()
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    }

    /**
     * Метод для создания объекта Pageable, используемого для пагинации списка пользователей.
     *
     * @param userPageDto объект типа UserPageDto, содержащий параметры пагинации.
     * @return объект типа Pageable, используемый для пагинации списка пользователей.
     * @throws BadRequestException если указаны некорректные параметры пагинации.
     */
    private Pageable createPageable(UserPageDto userPageDto) {
        int page = userPageDto.getPageNumber();
        int size = userPageDto.getPageSize();

        PageRequest pageRequest;

        Map<String, Sort.Direction> sortProperties = userPageDto.getSorts();

        if (sortProperties != null && sortProperties.size() > 0) {
            List<Sort.Order> orders = new ArrayList<>();

            for (Map.Entry<String, Sort.Direction> entry : sortProperties.entrySet()) {
                Sort.Direction direction = entry.getValue();
                String property = entry.getKey();

                Sort.Order order;
                if (direction.toString().equals("ASC")) {
                    order = Sort.Order.asc(property);
                } else {
                    order = Sort.Order.desc(property);
                }
                orders.add(order);
            }

            Sort sort = Sort.by(orders);
            pageRequest = PageRequest.of(page, size, sort);

        } else {
            pageRequest = PageRequest.of(page, size);
        }

        return pageRequest;
    }

}