package com.event.booking.management.DTO;

import lombok.Data;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Data
public class BookingRequestDTO {

    @NotBlank
    private String userName;

    @Pattern(regexp = "\\d{10}", message = "Mobile must be 10 digits")
    private String mobile;

    @Email
    private String email;

    @NotNull(message = "Seat type ID is required")
    private Long seatTypeId;  

    @Min(1)
    private Integer numberOfSeats;


    @NotNull(message = "Event ID is required")
    private Long eventId;

    @NotBlank(message = "Status is required")
    private String status;

    // getters and setters
}
