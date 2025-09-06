package com.event.booking.management.DTO;

import java.time.LocalDate;

import com.event.booking.management.model.Event.EventStatus;

import jakarta.validation.constraints.*;


public class UpdateEvent {

	 @Size(min = 1, max = 255, message = "Event name must be between 1-255 characters")
	    private String eventName;

	    @Future(message = "Event date must be in the future")
	    private LocalDate eventDate;

	    @NotBlank(message = "Location cannot be blank")
	    private String location;

	    @Min(value = 1, message = "Capacity must be at least 1")
	    private Integer capacity;

	    @NotBlank(message = "Description cannot be blank")
	    private String description;

	    private EventStatus status;

	    // Getters and Setters
	    public String getEventName() { return eventName; }
	    public void setEventName(String eventName) { this.eventName = eventName; }

	    public LocalDate getEventDate() { return eventDate; }
	    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }

	    public String getLocation() { return location; }
	    public void setLocation(String location) { this.location = location; }

	    public Integer getCapacity() { return capacity; }
	    public void setCapacity(Integer capacity) { this.capacity = capacity; }

	    public String getDescription() { return description; }
	    public void setDescription(String description) { this.description = description; }

	    public EventStatus getStatus() { return status; }
	    public void setStatus(EventStatus status) { this.status = status; }
}