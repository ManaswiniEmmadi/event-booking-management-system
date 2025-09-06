package com.event.booking.management.DTO;

import jakarta.validation.constraints.*;



import java.time.LocalDate;
// this dto takes 6 attributes
public class CreateEvent {

    @NotBlank
    private String eventName;

    @Future
    private LocalDate eventDate;

    @NotBlank
    private String location;

    @NotNull
    @Min(1)
    private Integer capacity;

    private String description;

    private Long organizerId;

//  Getters and Setters
	

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public LocalDate getEventDate() {
		return eventDate;
	}

	public void setEventDate(LocalDate eventDate) {
		this.eventDate = eventDate;
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

	public Long getOrganizerId() {
		return organizerId;
	}

	public void setOrganizerId(Long organizerId) {
		this.organizerId = organizerId;
	}

    
    
}
