package com.event.booking.management.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatTypeResponseDTO {
    private Long id;
    private String seatType;
    private double price;
    private int totalSeats;
    private int availableSeats;
    private String eventName;
}
