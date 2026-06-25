package com.sipokar.webapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sipokar.webapp.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findTop10ByOrderByCreatedAtDesc();

    long countByIsReadFalse();
}