package com.event.booking.management.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    private String userName;
    private String mobile;
    private String email;
    @ManyToOne
    @JoinColumn(name = "seat_type_id")
    private SeatType seatType;

    private Integer numberOfSeats;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", insertable = false, updatable = false)
    private Event event;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    private String status; // "pending", "confirmed", "cancelled"
}
