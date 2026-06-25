package com.sipokar.webapp.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.sipokar.webapp.service.NotificationService;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributes {

    private final NotificationService notificationService;

    @ModelAttribute("unreadCount")
    public long unreadCount() {
        return notificationService.countUnread();
    }
}