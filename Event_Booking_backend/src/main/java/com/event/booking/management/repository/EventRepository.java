package com.event.booking.management.repository;

import java.time.LocalDate;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.event.booking.management.model.Event;
import com.event.booking.management.model.Event.EventStatus;

public interface EventRepository extends JpaRepository<Event, Long> {
	List<Event> findByStatus(EventStatus status);
    List<Event> findByEventDate(LocalDate eventDate);
    List<Event> findByOrganizer_UserId(Long userId);

}
