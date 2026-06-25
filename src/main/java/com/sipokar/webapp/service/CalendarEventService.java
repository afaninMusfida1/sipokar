package com.sipokar.webapp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sipokar.webapp.model.CalendarEvent;
import com.sipokar.webapp.repository.CalendarEventRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CalendarEventService {

    private final CalendarEventRepository repository;

    public List<CalendarEvent> findAll() {
        return repository.findAll();
    }

    public CalendarEvent findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agenda tidak ditemukan"));
    }

    public void save(CalendarEvent event) {
        repository.save(event);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}