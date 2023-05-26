package ru.tsu.hits.messengerapi.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsu.hits.messengerapi.common.dto.NewNotificationDto;
import ru.tsu.hits.messengerapi.common.exception.NotFoundException;
import ru.tsu.hits.messengerapi.notification.dto.ChangeStatusNotificationDto;
import ru.tsu.hits.messengerapi.notification.dto.NotificationFiltersDto;
import ru.tsu.hits.messengerapi.notification.dto.NotificationPageDto;
import ru.tsu.hits.messengerapi.notification.dto.PageDto;
import ru.tsu.hits.messengerapi.notification.entity.NotificationEntity;
import ru.tsu.hits.messengerapi.notification.enumeration.NotificationStatus;
import ru.tsu.hits.messengerapi.notification.mapper.NotificationMapper;
import ru.tsu.hits.messengerapi.notification.repository.NotificationRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static ru.tsu.hits.messengerapi.notification.specification.NotificationSpecification.*;

/**
 * Сервис для работы с уведомлениями.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    /**
     * Возвращает количество непрочитанных уведомлений для указанного пользователя.
     *
     * @param targetUserId идентификатор пользователя
     * @return количество непрочитанных уведомлений
     */
    @Transactional(readOnly = true)
    public int getUnreadNotifications(UUID targetUserId) {

        return notificationRepository.countAllByUserIdAndReadDate(targetUserId, null);
    }

    /**
     * Изменяет статус уведомлений и возвращает количество непрочитанных уведомлений для указанного пользователя.
     *
     * @param changeStatusNotificationDto DTO, содержащий информацию о статусе уведомлений
     * @param targetUserId                идентификатор пользователя
     * @return количество непрочитанных уведомлений
     */
    @Transactional
    public int changeStatusNotifications(ChangeStatusNotificationDto changeStatusNotificationDto, UUID targetUserId) {

        List<NotificationEntity> notificationEntities = new ArrayList<>();

        for (UUID id : changeStatusNotificationDto.getNotificationIds()) {
            NotificationEntity notificationEntity = findNotification(id);
            notificationEntities.add(notificationEntity);
        }
        if (changeStatusNotificationDto.getNotificationStatus() == NotificationStatus.READ) {
            setRead(notificationEntities);
        } else {
            setUnread(notificationEntities);
        }
        notificationRepository.saveAll(notificationEntities);
        return getUnreadNotifications(targetUserId);
    }


    /**
     * Получает страницу уведомлений для указанного пользователя и фильтров.
     *
     * @param pageDto      объект, содержащий информацию о запрашиваемой странице
     * @param targetUserId идентификатор пользователя, для которого запрашиваются уведомления
     * @return объект, содержащий список уведомлений и информацию о фильтрах
     */
    @Transactional(readOnly = true)
    public NotificationPageDto getNotifications(PageDto pageDto, UUID targetUserId) {

        Pageable pageable = PageRequest.of(
                pageDto.getPageableDto().getPageNumber(),
                pageDto.getPageableDto().getPageSize(),
                Sort.by(Sort.Direction.DESC, "receivingDate")
        );

        Specification<NotificationEntity> specification = buildSpecification(targetUserId, pageDto.getNotificationFiltersDto());

        return new NotificationPageDto(
                notificationRepository.findAll(
                        specification,
                        pageable
                ).map(notificationMapper::entityToNotificationDto),
                pageDto.getNotificationFiltersDto()

        );
    }

    /**
     * Создает новое уведомление на основе переданных данных.
     *
     * @param newNotificationDto объект, содержащий информацию о новом уведомлении
     */
    @Transactional
    public void createNotification(NewNotificationDto newNotificationDto) {
        notificationRepository.save(notificationMapper.dtoToNotificationEntity(newNotificationDto));
    }


    /**
     * Строит спецификацию для фильтрации уведомлений по заданным параметрам.
     *
     * @param userId  идентификатор пользователя, для которого запрашиваются уведомления
     * @param filters объект, содержащий информацию о фильтрах
     * @return спецификация для фильтрации уведомлений
     */
    private Specification<NotificationEntity> buildSpecification(UUID userId, NotificationFiltersDto filters) {

        Specification<NotificationEntity> specification = userId(userId);
        if (filters != null) {
            if (filters.getTextFilter() != null && !filters.getTextFilter().isEmpty() && !filters.getTextFilter()
                    .isBlank()) {
                specification = specification.and(messageContainsSubstring(filters.getTextFilter()));
            }
            if (filters.getNotificationTypes() != null && !filters.getNotificationTypes().isEmpty()) {
                specification = specification.and(typeIn(filters.getNotificationTypes()));
            }
            if (filters.getPeriodFilter() != null && filters.getPeriodFilter().getFromDateTime() != null) {
                specification = specification.and(receivedDateTimeAfter(filters.getPeriodFilter().getFromDateTime()));
            }
            if (filters.getPeriodFilter() != null && filters.getPeriodFilter().getToDateTime() != null) {
                specification = specification.and(receivedDateTimeBefore(filters.getPeriodFilter().getToDateTime()));
            }
        }
        return specification;
    }

    /**
     * Ищет уведомление по его идентификатору.
     *
     * @param notificationId идентификатор уведомления
     * @return объект уведомления
     * @throws NotFoundException если уведомление не найдено
     */
    @Transactional(readOnly = true)
    public NotificationEntity findNotification(UUID notificationId) {
        return notificationRepository.findById(notificationId).orElseThrow(() -> {
            throw new NotFoundException("Уведомление не найдено.");
        });
    }

    /**
     * Устанавливает дату прочтения уведомлений в текущее время.
     *
     * @param notifications список уведомлений, для которых нужно установить дату прочтения
     */
    private void setRead(List<NotificationEntity> notifications) {
        LocalDateTime dateTime = LocalDateTime.now();
        for (NotificationEntity notification : notifications) {
            notification.setReadDate(dateTime);
            notification.setRead(true);
        }
    }

    /**
     * Сбрасывает дату прочтения уведомлений на null.
     *
     * @param notifications список уведомлений, для которых нужно сбросить дату прочтения
     */
    private void setUnread(List<NotificationEntity> notifications) {
        for (NotificationEntity notification : notifications) {
            notification.setReadDate(null);
        }
    }

}
