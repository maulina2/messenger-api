package ru.tsu.hits.messengerapi.friends.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tsu.hits.messengerapi.friends.entity.FriendEntity;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с сущностью пользователя, добавленного в друзья.
 */
@Repository
public interface FriendRepository extends JpaRepository<FriendEntity, UUID> {

    /**
     * Метод для поиска дружбы между целевым и внешним пользователями.
     *
     * @param targetUserId   идентификатор целевого пользователя
     * @param externalUserId идентификатор внешнего пользователя
     * @return объект Optional класса FriendEntity, содержащий дружбу или null, если дружба не найдена
     */
    Optional<FriendEntity> findByTargetUserAndExternalUser(UUID targetUserId, UUID externalUserId);

    /**
     * Метод для проверки существования дружбы между целевым и внешним пользователями.
     *
     * @param targetUserId   идентификатор целевого пользователя
     * @param externalUserId идентификатор внешнего пользователя
     * @return true, если дружба существует, иначе false
     */
    boolean existsByTargetUserAndExternalUser(UUID targetUserId, UUID externalUserId);

    /**
     * Метод для поиска всех друзей целевого пользователя с использованием пагинации.
     *
     * @param targetUserId идентификатор целевого пользователя
     * @param pageable     объект класса Pageable, содержащий информацию о странице и количестве элементов на странице
     * @return объект класса Page, содержащий список объектов класса FriendEntity и информацию о странице
     */
    Page<FriendEntity> findByTargetUser(UUID targetUserId, Pageable pageable);

    /**
     * Метод для поиска всех друзей внешнего пользователя.
     *
     * @param externalUserId идентификатор внешнего пользователя
     * @return список объектов класса FriendEntity, содержащий всех друзей или пустой список, если друзья не найдены
     */
    List<FriendEntity> findByExternalUser(UUID externalUserId);

    /**
     * Метод для поиска всех друзей целевого пользователя по полному имени пользователя и дате удаления дружбы с использованием пагинации.
     *
     * @param targetUserId        идентификатор целевого пользователя
     * @param fullName            полное имя пользователя
     * @param friendRemovalDate дата удаления дружбы
     * @param pageable            объект класса Pageable, содержащий информацию о странице и количестве элементов на странице
     * @return объект класса Page, содержащий список объектов класса FriendEntity и информацию о странице
     */
    Page<FriendEntity> findByTargetUserAndFullNameLikeAndFriendRemovalDate(UUID targetUserId,
                                                                           String fullName,
                                                                           Date friendRemovalDate,
                                                                           Pageable pageable);
}
