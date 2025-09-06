package com.event.booking.management.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingStatusUpdateDTO {
    
    @NotBlank(message = "Status is required")
    private String status;

	
}
