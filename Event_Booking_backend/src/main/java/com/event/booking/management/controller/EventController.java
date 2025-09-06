package com.event.booking.management.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*; //DeleteMapping,GetMapping,PathVariable,PostMapping,PutMapping,RequestBody,RequestMapping,RequestParam,RestController
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.event.booking.management.DTO.BookingResponseDTO;
import com.event.booking.management.DTO.CreateEvent;
import com.event.booking.management.DTO.FullEventDto;
import com.event.booking.management.DTO.UpdateEvent;
import com.event.booking.management.exception.EventNotFoundException;
import com.event.booking.management.model.Event;
import com.event.booking.management.service.BookingService;
import com.event.booking.management.service.EventService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/event")
@CrossOrigin(origins = "http://localhost:4200")
public class EventController {
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private BookingService bookingService;
	
	@PreAuthorize("hasRole('ORGANIZER')")
	@PostMapping("/create")
	public ResponseEntity<String> createEvent(@Valid @RequestBody CreateEvent event) {
		
		 eventService.createEvent(event);
		
        return new ResponseEntity<>(event.getEventName()+"Event created successfully",HttpStatus.OK);
        
    }
	
	@GetMapping("/all")
    public ResponseEntity<List<FullEventDto>> getAllEvents() {
        return new ResponseEntity<>(eventService.getAllEvents(),HttpStatus.OK);
    }
	@GetMapping("/my")
    public List<FullEventDto> getMyEvents() {
        return eventService.getEventsForLoggedInOrganizer();
    }
	
	@GetMapping("/get/{eventId}")
    public ResponseEntity<FullEventDto> getEvent(@PathVariable Long eventId) {
        return new ResponseEntity<>(eventService.getEvent(eventId),HttpStatus.OK);
    }
	
	@GetMapping("/filter/status")
    public ResponseEntity<List<Event>> filterByStatus(@RequestParam String status) {
        return new ResponseEntity<>(eventService.filterEventsByStatus(status),HttpStatus.OK);
    }

    @GetMapping("/filter/date")
    public ResponseEntity<List<Event>> filterByDate(@RequestParam String date) {
        return new ResponseEntity<>(eventService.filterEventsByDate(LocalDate.parse(date)),HttpStatus.OK);
    }
 
    @PreAuthorize("hasRole('ORGANIZER')")
    @GetMapping("/{eventId}/bookings")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByEvent(@PathVariable Long eventId) {
        List<BookingResponseDTO> bookings = bookingService.getBookingsForEvent(eventId);
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }
    
    @PreAuthorize("hasRole('ORGANIZER')")
    @PutMapping("/update/{eveid}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long eveid,@Valid @RequestBody UpdateEvent request) throws Exception {
        return new ResponseEntity<>(eventService.updateEvent(eveid, request),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ORGANIZER')")
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long id) throws Exception {
    	try {
        eventService.deleteEvent(id);
    	}catch(EventNotFoundException e) {
    		return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
    	}
        return new ResponseEntity<>("id "+id+" is successfully deleted",HttpStatus.OK);
    }

}
