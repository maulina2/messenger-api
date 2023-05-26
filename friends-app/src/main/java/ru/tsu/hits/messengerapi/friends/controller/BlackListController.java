package ru.tsu.hits.messengerapi.friends.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.messengerapi.friends.dto.BlackListPageDto;
import ru.tsu.hits.messengerapi.friends.dto.BlockedUserDto;
import ru.tsu.hits.messengerapi.friends.dto.SearchBlockedUserDto;
import ru.tsu.hits.messengerapi.friends.service.BlackListService;

import javax.validation.Valid;
import java.util.UUID;

import static ru.tsu.hits.messengerapi.commonsecurity.service.JwtService.extractId;


/**
 * Контроллер для обработки запросов, связанных с черным списком в сервисе друзей.
 */
@RestController
@RequestMapping("/api/v1/friends/black-list")
@RequiredArgsConstructor
@Tag(name = "Черный список")
public class BlackListController {

    private final BlackListService blackListService;


    /**
     * Добавляет пользователя в черный список.
     *
     * @param id идентификатор пользователя, которого нужно добавить в черный список
     * @return DTO с информацией о добавленном пользователе в черный список.
     */
    @Operation(
            summary = "Добавить пользователя в черный список.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/{id}")
    public BlockedUserDto blockUser(@PathVariable UUID id, Authentication authentication) {

        return blackListService.addBlockedUser(id, extractId(authentication));
    }

    /**
     * Возвращает информацию о пользователе в черном списке.
     *
     * @param id ID пользователя в черном списке.
     * @return DTO с информацией о пользователе в черном списке.
     */
    @Operation(
            summary = "Просмотреть пользователя в черном списке.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/{id}")
    public BlockedUserDto getBlockedUser(@PathVariable UUID id, Authentication authentication) {


        return blackListService.getBlockedUser(id, extractId(authentication));
    }

    /**
     * Удаляет пользователя из черного списка.
     *
     * @param id ID пользователя, которого нужно удалить из черного списка.
     */
    @Operation(
            summary = "Удалить пользователя из черного списка.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/{id}")
    public void deleteBlockedUser(@PathVariable UUID id, Authentication authentication) {

        blackListService.deleteBlockedUser(id, extractId(authentication));
    }


    /**
     * Проверяет, находится ли пользователь в черном списке.
     *
     * @param id ID пользователя, которого нужно проверить на нахождение в черном списке.
     * @return true, если пользователь находится в черном списке, false - если нет.
     */
    @Operation(
            summary = "Проверка на нахождение пользователя в черном списке.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/is_blocked/{id}")
    public boolean isBlockedUser(@PathVariable UUID id, Authentication authentication) {

        return blackListService.isBlockedUser(id, extractId(authentication));
    }

    /**
     * Возвращает страницу с информацией о пользователях в черном списке.
     *
     * @param blackListPageDto DTO с информацией о странице.
     * @return страница с информацией о пользователях в черном списке.
     */
    @Operation(
            summary = "Просмотреть черный список.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/page")
    public Page<BlockedUserDto> getBlackListPage(@RequestBody @Valid BlackListPageDto blackListPageDto, Authentication authentication) {

        return blackListService.getBlackListPage(blackListPageDto, extractId(authentication));
    }

    /**
     * Метод осуществляет поиск пользователей в черном списке.
     *
     * @param searchBlockedUserDto объект типа SearchBlockedUserDto, содержащий параметры поиска пользователей в черном списке.
     * @return страницу объектов типа BlockedUserDto, содержащую найденных пользователей в черном списке.
     */
    @Operation(
            summary = "Осуществить поиск пользователей в черном списке.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/search")
    public Page<BlockedUserDto> searchBlockedUser(@RequestBody @Valid SearchBlockedUserDto searchBlockedUserDto, Authentication authentication) {

        return blackListService.searchBlockedUser(searchBlockedUserDto, extractId(authentication));
    }
}
