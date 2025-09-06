package com.event.booking.management.DTO;

import java.time.LocalDate;

import lombok.Data;
@Data
public class FullEventDto {

	private Long eventId;
	private String eventName;
    private LocalDate eventDate;
    private String location;
    private int capacity;
    private String description;
    private String status;
    private String organizerName;   // fullName from profile
    private String organizerEmail;  // email from User
    private String organizerPhone;  // phoneNumber from profile
    
}
