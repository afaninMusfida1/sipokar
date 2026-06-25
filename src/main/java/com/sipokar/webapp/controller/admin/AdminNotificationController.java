package com.sipokar.webapp.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.sipokar.webapp.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/notifications")
@RequiredArgsConstructor
public class AdminNotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("notifications", notificationService.findLatest());
        model.addAttribute("unreadCount", notificationService.countUnread());
        return "admin/notifications";
    }

    @PostMapping("/read/{id}")
    public String read(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return "redirect:/admin/notifications";
    }

    @PostMapping("/read-all")
    public String readAll() {
        notificationService.markAllAsRead();
        return "redirect:/admin/notifications";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        notificationService.delete(id);
        return "redirect:/admin/notifications";
    }
}