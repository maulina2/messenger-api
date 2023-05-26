package ru.tsu.hits.messengerapi.friends.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tsu.hits.messengerapi.commonsecurity.dto.JwtUserData;
import ru.tsu.hits.messengerapi.friends.dto.FriendDto;
import ru.tsu.hits.messengerapi.friends.entity.FriendEntity;

import java.util.UUID;

/**
 * Класс для маппинга различных dto и {@link FriendEntity}.
 */
@Component
@RequiredArgsConstructor
public class FriendMapper {

    /**
     * Метод addFriendDtoToFriendEntity преобразует объект типа AddFriendDto в объект типа FriendEntity.
     *
     * @param externalUserId идентификатор пользователя, которого добавили в друзья
     * @param targetUserId UUID идентификатор целевого пользователя
     * @return объект типа FriendEntity, полученный в результате преобразования
     */
    public FriendEntity addFriendDtoToFriendEntity(UUID externalUserId, String fullName, UUID targetUserId){
        return FriendEntity
                .builder()
                .externalUser(externalUserId)
                .fullName(fullName)
                .targetUser(targetUserId)
                .build();
    }

    /**
     * Метод JwtUserDataToFriendEntity преобразует объект типа JwtUserData в объект типа FriendEntity.
     *
     * @param userData объект JwtUserData, который необходимо преобразовать
     * @param targetUserId UUID идентификатор целевого пользователя
     * @return объект типа FriendEntity, полученный в результате преобразования
     */
    public FriendEntity JwtUserDataToFriendEntity(JwtUserData userData, UUID targetUserId){
        return FriendEntity
                .builder()
                .externalUser(userData.getId())
                .fullName(userData.getFullName())
                .targetUser(targetUserId)
                .build();
    }


    /**
     * Метод friendEntityToFriendDto преобразует объект типа FriendEntity в объект типа FriendDto.
     *
     * @param friend объект типа FriendEntity, который необходимо преобразовать
     * @return объект типа FriendDto, полученный в результате преобразования
     */
    public FriendDto friendEntityToFriendDto(FriendEntity friend){
        return new FriendDto(
                friend.getId(),
                friend.getFullName(),
                friend.getExternalUser(),
                friend.getFriendAddedDate(),
                friend.getFriendRemovalDate()
        );
    }
}