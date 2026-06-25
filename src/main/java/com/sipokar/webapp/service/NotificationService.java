package com.sipokar.webapp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sipokar.webapp.model.Notification;
import com.sipokar.webapp.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;

    public List<Notification> findLatest() {
        return repository.findTop10ByOrderByCreatedAtDesc();
    }

    public long countUnread() {
        return repository.countByIsReadFalse();
    }

    public void save(String message) {
        repository.save(
                Notification.builder()
                        .message(message)
                        .isRead(false)
                        .build());
    }

    public void markAsRead(Long id) {
        Notification notif = repository.findById(id).orElseThrow();
        notif.setIsRead(true);
        repository.save(notif);
    }

    public void markAllAsRead() {
        List<Notification> notifications = repository.findAll();

        for (Notification notif : notifications) {
            notif.setIsRead(true);
        }

        repository.saveAll(notifications);
    }

    public void delete(Long id) {
        Notification notif = repository.findById(id).orElseThrow();

        if (notif.getIsRead()) {
            repository.delete(notif);
        }
    }
}