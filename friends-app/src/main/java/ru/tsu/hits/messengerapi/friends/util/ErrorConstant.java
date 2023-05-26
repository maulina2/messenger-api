package ru.tsu.hits.messengerapi.friends.util;

/**
 * Класс в котором хранятся константы для выбрасывания ошибок.
 */
public class ErrorConstant {
    public static final String ALREADY_REMOVED_FROM_FRIENDS = "Данный пользователь уже удален из друзей.";
    public static final String ALREADY_REMOVED_FROM_BLACKLIST = "Данный пользователь уже удален из черного списка.";
    public static final String NOT_FOUND_IN_BLACKLIST = "Данный пользователь в черном списке не найден.";
    public static final String NOT_FOUND_IN_FRIENDS = "Данный пользователь в друзьях не найден.";
    public static final String ALREADY_ADDED_TO_FRIENDS = "Данный пользователь уже добавлен в друзья.";
    public static final String ALREADY_ADDED_TO_BLACKLIST = "Данный пользователь уже добавлен в черный список.";
    public static final String ADDING_YOURSELF_TO_THE_BLACKLIST = "Вы не можете добавить в черный список самого себя.";
    public static final String ADDING_YOURSELF_TO_THE_FRIENDS = "Вы не можете добавить в друзья самого себя.";
}
