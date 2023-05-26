package ru.tsu.hits.messengerapi.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tsu.hits.messengerapi.user.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с сущностью UserEntity
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    /**
     * Метод проверяет наличие пользователя с указанным логином в базе данных.
     *
     * @param login логин пользователя
     * @return true, если пользователь с указанным логином существует в базе данных, иначе false
     */
    boolean existsByLogin(String login);

    /**
     * Метод проверяет наличие пользователя с указанным email в базе данных.
     *
     * @param email email пользователя
     * @return true, если пользователь с указанным email существует в базе данных, иначе false
     */
    boolean existsByEmail(String email);

    /**
     * Метод находит пользователя с указанным логином в базе данных.
     *
     * @param login логин пользователя
     * @return объект Optional, содержащий найденного пользователя, если такой есть в базе данных, иначе пустой Optional
     */
    Optional<UserEntity> findByLogin(String login);
}