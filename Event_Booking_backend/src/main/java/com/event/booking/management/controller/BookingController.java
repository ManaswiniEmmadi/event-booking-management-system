package com.event.booking.management.controller;



import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.event.booking.management.DTO.BookingRequestDTO;
import com.event.booking.management.DTO.BookingResponseDTO;
import com.event.booking.management.service.BookingService;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:4200")
public class BookingController {

    @Autowired
    private BookingService bookingService;
    
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/addBooking")
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingRequestDTO dto) {
        
        BookingResponseDTO response = bookingService.createBooking(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
        
    }


    @GetMapping("/viewAllBookings")
    public ResponseEntity<List<BookingResponseDTO>> getAllBookings() {
        return new ResponseEntity<>(bookingService.getAllBookings(), HttpStatus.OK);
    }
    
    @GetMapping("/myBookings")
    public ResponseEntity<List<BookingResponseDTO>> getMyBookings() {
        return new ResponseEntity<>(bookingService.getBookingsForLoggedInUser(), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingById(@PathVariable Long id) {
        return new ResponseEntity<>(bookingService.getBookingById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ORGANIZER')")
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByEventId(@PathVariable Long eventId) {
        return new ResponseEntity<>(bookingService.getBookingsByEventId(eventId), HttpStatus.OK);
    }
    
    @GetMapping("/by-status")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByStatus(@RequestParam String status) {
        return new ResponseEntity<>(bookingService.getBookingsByStatus(status), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/updateBooking/{id}")
    public ResponseEntity<BookingResponseDTO> updateBooking(@PathVariable Long id, @Valid @RequestBody BookingRequestDTO dto) {
        return new ResponseEntity<>(bookingService.updateBooking(id, dto), HttpStatus.OK);
    }
    
    
    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/{bookingId}/status")
    public ResponseEntity<BookingResponseDTO> updateBookingStatus(
            @PathVariable Long bookingId,
            @RequestParam String status) {

        BookingResponseDTO updatedBooking = bookingService.updateBookingStatus(bookingId, status);
        return ResponseEntity.ok(updatedBooking);
    }



    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBookingById(@PathVariable Long id) {
        bookingService.deleteBookingById(id);
        return new ResponseEntity<>("Booking deleted successfully", HttpStatus.NO_CONTENT);
    }
}
