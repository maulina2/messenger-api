package ru.tsu.hits.messengerapi.friends.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.messengerapi.friends.dto.FriendDto;
import ru.tsu.hits.messengerapi.friends.dto.FriendPageDto;
import ru.tsu.hits.messengerapi.friends.dto.SearchFriendDto;
import ru.tsu.hits.messengerapi.friends.service.FriendService;

import javax.validation.Valid;
import java.util.UUID;

import static ru.tsu.hits.messengerapi.commonsecurity.service.JwtService.extractId;
import static ru.tsu.hits.messengerapi.commonsecurity.service.JwtService.extractPersonData;

/**
 * Контроллер для обработки запросов в сервисе друзей.
 */

@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
@Tag(name = "Друзья")
public class FriendController {

    private final FriendService friendService;

    /**
     * Метод для обработки запроса добавления пользователя в список друзей.
     *
     * @param id идентификатор пользователя, которого нужно добавить в друзья
     * @return объект типа FriendDto, содержащий данные о пользователе, добавленном в список друзей.
     */
    @Operation(
            summary = "Добавить в друзья пользователя.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/{id}")
    public FriendDto addFriend(Authentication authentication, @PathVariable UUID id) {

        return friendService.addFriend(id, extractPersonData(authentication));
    }

    /**
     * Метод для обработки запроса на просмотр пользователя, добавленного в друзья к текущему пользователю.
     *
     * @param id идентификатор пользователя, которого нужно просмотреть в списке друзей.
     * @return объект типа FriendDto, содержащий данные о пользователе, добавленном в список друзей.
     */
    @Operation(
            summary = "Просмотреть пользователя в друзьях.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/{id}")
    public FriendDto getFriend(@PathVariable UUID id,Authentication authentication) {

        return friendService.getFriend(id, extractId(authentication));
    }

    /**
     * Метод для обработки запроса на удаление пользователя из списка друзей.
     *
     * @param id идентификатор пользователя, которого нужно удалить из списка друзей.
     */
    @Operation(
            summary = "Удалить пользователя из друзей.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/{id}")
    public void deleteFriend(@PathVariable UUID id, Authentication authentication) {

        friendService.deleteFriends(id, extractId(authentication));
    }

    /**
     * Метод для обработки запроса на просмотр страницы пользователей, добавленных в друзья.
     *
     * @param friendPageDto объект типа FriendPageDto, содержащий данные о странице пользователей, добавленных в друзья.
     * @return страницу объектов типа FriendDto, содержащую данные о пользователях, добавленных в список друзей.
     */
    @Operation(
            summary = "Просмотреть список друзей.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/page")
    public Page<FriendDto> getFriendPage(@RequestBody @Valid FriendPageDto friendPageDto,Authentication authentication) {

        return friendService.getFriendPage(friendPageDto, extractId(authentication));
    }

    /**
     * Метод для обработки запроса на поиск пользователей, добавленных в друзья к текущему пользователю.
     *
     * @param searchFriendDto объект типа SearchFriendDto, содержащий параметры поиска пользователей, добавленных в друзья.
     * @return страницу объектов типа FriendDto, содержащую найденных пользователей, добавленных в список друзей.
     */
    @Operation(
            summary = "Осуществить поиск пользователей, добавленных в друзья.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/search")
    public Page<FriendDto> searchFriend(@RequestBody @Valid SearchFriendDto searchFriendDto, Authentication authentication) {

        return friendService.searchFriend(searchFriendDto, extractId(authentication));
    }
}
