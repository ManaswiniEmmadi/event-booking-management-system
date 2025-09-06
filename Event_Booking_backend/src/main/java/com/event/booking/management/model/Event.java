package com.event.booking.management.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity 
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @Column(nullable = false)
    private String eventName;

    @Column(nullable = false)
    private LocalDate eventDate;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Integer capacity;

    private String description;

    @Enumerated(EnumType.STRING)
    private EventStatus status = EventStatus.UPCOMING;
    
    public enum EventStatus {
    	UPCOMING,
    	ONGOING,
    	COMPLETED,
    	CANCELLED
    	
    }

    @ManyToOne
    @JoinColumn(name = "organizer_id", nullable = false)
    @JsonManagedReference
    private User organizer;

//    Getters and Setters
	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public LocalDate getEventDate() {
		return eventDate;
	}

	public void setEventDate(LocalDate localDate) {
		this.eventDate = localDate;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public EventStatus getStatus() {
		return status;
	}

	public void setStatus(EventStatus status) {
		this.status = status;
	}

	public User getOrganizer() {
		return organizer;
	}

	public void setOrganizer(User organizer) {
		this.organizer = organizer;
	}

    
}