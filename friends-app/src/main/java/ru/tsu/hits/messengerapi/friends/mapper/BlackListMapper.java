package ru.tsu.hits.messengerapi.friends.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tsu.hits.messengerapi.friends.dto.BlockedUserDto;
import ru.tsu.hits.messengerapi.friends.entity.BlockedUserEntity;

import java.util.UUID;

/**
 * Класс для маппинга различных dto и {@link BlockedUserEntity}.
 */
@Component
@RequiredArgsConstructor
public class BlackListMapper {

    /**
     * Метод addBlockedUserDtoToBlockedUserEntity преобразует объект типа AddBlockedUserDto в объект типа BlockedUserEntity.
     *
     * @param externalUserId идентификатор пользователя, которого нужно добавить в черный список.
     * @param targetUserId UUID идентификатор целевого пользователя
     * @return объект типа BlockedUserEntity, полученный в результате преобразования
     */
    public BlockedUserEntity addBlockedUserDtoToBlockedUserEntity(UUID externalUserId, String fullName, UUID targetUserId){
        return BlockedUserEntity
                .builder()
                .externalUser(externalUserId)
                .fullName(fullName)
                .targetUser(targetUserId)
                .build();
    }

    /**
     * Метод addBlockedUserDtoToBlockedUserEntity преобразует объект типа AddBlockedUserDto в объект типа BlockedUserEntity.
     *
     * @param friend объект BlockedUserEntity, который необходимо преобразовать
     * @return объект типа BlockedUserDto, полученный в результате преобразования
     */
    public BlockedUserDto blockedUserEntityToBlockedUserDto(BlockedUserEntity friend){
        return new BlockedUserDto(
                friend.getId(),
                friend.getFullName(),
                friend.getExternalUser(),
                friend.getBlockedUserAddedDate(),
                friend.getBlockedUserRemovalDate()
        );
    }
}
