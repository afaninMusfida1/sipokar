package com.sipokar.webapp.controller.admin;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import com.sipokar.webapp.model.CalendarEvent;
import com.sipokar.webapp.service.CalendarEventService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/calendar")
@RequiredArgsConstructor
public class AdminCalendarController {

    private final CalendarEventService service;

    @GetMapping
    public String index(Model model) {
        return "admin/calendar";
    }

    @GetMapping("/events")
    @ResponseBody
    public List<Map<String, Object>> getEvents() {
        return service.findAll().stream()
                .filter(event -> event.getEventDate() != null && event.getEventTime() != null)
                .map(event -> {
                    Map<String, Object> map = new HashMap<>();

                    map.put("id", event.getId());
                    map.put("title", event.getTitle());
                    map.put("description", event.getDescription());
                    map.put("type", event.getType());

                    map.put(
                            "start",
                            LocalDateTime.of(
                                    event.getEventDate(),
                                    event.getEventTime()));

                    return map;
                }).collect(Collectors.toList());
    }

    @PostMapping("/save")
    public String save(@ModelAttribute CalendarEvent event) {
        service.save(event);
        return "redirect:/admin/calendar";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute CalendarEvent updated) {
        CalendarEvent event = service.findById(id);

        event.setTitle(updated.getTitle());
        event.setDescription(updated.getDescription());
        event.setEventDate(updated.getEventDate());
        event.setEventTime(updated.getEventTime());
        event.setType(updated.getType());

        service.save(event);

        return "redirect:/admin/calendar";
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "success";
    }
}