package com.sipokar.webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sipokar.webapp.model.CalendarEvent;

public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {
}