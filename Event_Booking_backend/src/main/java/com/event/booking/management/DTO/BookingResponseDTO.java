package com.event.booking.management.DTO;

import lombok.Data;
import java.util.List;

@Data
public class BookingResponseDTO {
    private Long bookingId;
    private String userName;
    private String mobile;
    private String email;
    private String seatType; // Seat name
    private Double price;    // Seat price
    private Integer numberOfSeats;
    private List<String> selectedSeats;
    private long eventId;
    private String status;
    
    private String eventName;
    private String eventDate;
    private String location;


    
	
}
