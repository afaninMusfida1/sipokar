package com.sipokar.webapp.service;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sipokar.webapp.model.CalendarEvent;
import com.sipokar.webapp.model.Notification;
import com.sipokar.webapp.repository.CalendarEventRepository;
import com.sipokar.webapp.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CalendarNotificationService {

    private final CalendarEventRepository calendarRepo;
    private final NotificationRepository notificationRepo;

    @Scheduled(cron = "*/5 * * * * *")
    public void checkEvents() {
        LocalDateTime now = LocalDateTime.now();

        for (CalendarEvent event : calendarRepo.findAll()) {
            LocalDateTime eventTime = LocalDateTime.of(
                    event.getEventDate(),
                    event.getEventTime());

            if (!event.getNotified() && !eventTime.isAfter(now)) {
                Notification notif = Notification.builder()
                        .message("Agenda \"" + event.getTitle() + "\" dimulai sekarang.")
                        .isRead(false)
                        .build();

                notificationRepo.save(notif);

                event.setNotified(true);
                calendarRepo.save(event);
            }
        }
    }
}