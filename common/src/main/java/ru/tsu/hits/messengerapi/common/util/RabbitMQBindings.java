package ru.tsu.hits.messengerapi.common.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RabbitMQBindings {
    public static final String CREATE_NOTIFICATION_OUT = "createNotification-out-0";
    public static final String SYNCHRONIZE_USER_INFO_OUT = "synchronizeUserInfo-out-0";
}