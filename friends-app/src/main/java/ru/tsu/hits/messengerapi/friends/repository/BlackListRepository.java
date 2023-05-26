package ru.tsu.hits.messengerapi.friends.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tsu.hits.messengerapi.friends.entity.BlockedUserEntity;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с сущностью заблокированного пользователя.
 */
@Repository
public interface BlackListRepository extends JpaRepository<BlockedUserEntity, UUID> {

    /**
     * Метод для поиска заблокированного пользователя по идентификаторам целевого пользователя и внешнего пользователя.
     *
     * @param targetUserId   идентификатор целевого пользователя
     * @param externalUserId идентификатор внешнего пользователя
     * @return объект Optional класса BlockedUserEntity, содержащий заблокированного пользователя или null, если пользователь не найден
     */
    Optional<BlockedUserEntity> findByTargetUserAndExternalUser(UUID targetUserId, UUID externalUserId);

    /**
     * Метод для поиска заблокированного пользователя по идентификаторам целевого пользователя, внешнего пользователя и дате удаления блокировки.
     *
     * @param targetUserId        идентификатор целевого пользователя
     * @param externalUserId      идентификатор внешнего пользователя
     * @param removalDate дата удаления блокировки
     * @return объект Optional класса BlockedUserEntity, содержащий заблокированного пользователя или null, если пользователь не найден
     */
    Optional<BlockedUserEntity> findByTargetUserAndExternalUserAndBlockedUserRemovalDate(UUID targetUserId, UUID externalUserId, Date removalDate);

    /**
     * Метод для поиска всех заблокированных пользователей по идентификатору внешнего пользователя.
     *
     * @param externalUserId идентификатор внешнего пользователя
     * @return список объектов класса BlockedUserEntity, содержащий всех заблокированных пользователей или пустой список, если пользователи не найдены
     */
    List<BlockedUserEntity> findByExternalUser(UUID externalUserId);

    /**
     * Метод для проверки существования заблокированного пользователя по идентификаторам внешнего пользователя, целевого пользователя и дате удаления блокировки.
     *
     * @param externalUserId        идентификатор внешнего пользователя
     * @param targetUserId          идентификатор целевого пользователя
     * @param blockedUserRemovalDate дата удаления блокировки
     * @return true, если заблокированный пользователь существует, иначе false
     */
    boolean existsByExternalUserAndTargetUserAndBlockedUserRemovalDate(UUID externalUserId,
                                                                       UUID targetUserId,
                                                                       Date blockedUserRemovalDate);

    /**
     * Метод для поиска всех заблокированных пользователей по идентификатору целевого пользователя с использованием пагинации.
     *
     * @param targetUserId идентификатор целевого пользователя
     * @param pageable     объект класса Pageable, содержащий информацию о странице и количестве элементов на странице
     * @return объект класса Page, содержащий список объектов класса BlockedUserEntity и информацию о странице
     */
    Page<BlockedUserEntity> findByTargetUser(UUID targetUserId, Pageable pageable);

    /**
     * Метод для поиска всех заблокированных пользователей по идентификатору целевого пользователя, полному имени пользователя и дате удаления блокировки с использованием пагинации.
     *
     * @param targetUserId        идентификатор целевого пользователя
     * @param fullName            полное имя пользователя
     * @param removalDate дата удаления блокировки
     * @param pageable            объект класса Pageable, содержащий информацию о пагинации
     * @return объект класса Page, содержащий список объектов класса BlockedUserEntity и информацию о странице
     */
    Page<BlockedUserEntity> findByTargetUserAndFullNameLikeAndBlockedUserRemovalDate(UUID targetUserId,
                                                                                     String fullName,
                                                                                     Date removalDate,
                                                                                     Pageable pageable);
}
