package ru.tsu.hits.messengerapi.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsu.hits.messengerapi.common.exception.NotFoundException;
import ru.tsu.hits.messengerapi.user.entity.UserEntity;
import ru.tsu.hits.messengerapi.user.repository.UserRepository;

import java.util.UUID;

/**
 * Сервис для поиск пользователя.
 */
@Service
@RequiredArgsConstructor
public class FindUserService {

    private final UserRepository userRepository;

    /**
     * Метод для получения пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя.
     * @return объект типа UserEntity, содержащий информацию о пользователе.
     * @throws NotFoundException, если пользователь не найден.
     */
    @Transactional(readOnly = true)
    public UserEntity getUserById(UUID id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> {
                    throw new NotFoundException("Пользователь с таким id " + id.toString() + "не найден.");
                });
    }
}
