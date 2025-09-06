package com.event.booking.management.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.event.booking.management.DTO.CreateEvent;
import com.event.booking.management.DTO.FullEventDto;
import com.event.booking.management.DTO.UpdateEvent;
import com.event.booking.management.exception.EventNotFoundException;
import com.event.booking.management.model.Event;
import com.event.booking.management.model.Event.EventStatus;
import com.event.booking.management.model.User;
import com.event.booking.management.model.UserDetailsImpl;
import com.event.booking.management.repository.EventRepository;
import com.event.booking.management.repository.UserRepository;

@Service
public class EventService {

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private UserRepository userRepository;

	
	public Event createEvent(CreateEvent request) {

//		if (request.getOrganizerId() == null) {
//			throw new RuntimeException("Organizer ID is required");
//		}
				
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long loggedInUserId = userDetails.getId();
        
        User organizer = userRepository.findById(loggedInUserId)
				.orElseThrow(() -> new RuntimeException("Organizer not found"));
//
//		
//		if (organizer.getRole() != Role.ORGANIZER) {
//			throw new RuntimeException("You are not a ORGANIZER to create events");
//		}

		Event event = new Event();
		event.setEventName(request.getEventName());
		event.setEventDate(request.getEventDate());
		event.setLocation(request.getLocation());
		event.setCapacity(request.getCapacity());
		event.setDescription(request.getDescription());
		event.setOrganizer(organizer);
		return eventRepository.save(event);
	}

	public List<FullEventDto> getAllEvents() {
		return eventRepository.findAll()
	            .stream()
	            .map(this::toSummary)
	            .collect(Collectors.toList());
	}
	
	// Get only events created by the logged-in organizer
    public List<FullEventDto> getEventsForLoggedInOrganizer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long loggedInUserId = userDetails.getId();

        List<Event> events = eventRepository.findByOrganizer_UserId(loggedInUserId);
        return events.stream()
                .map(this::toDetail)
                .collect(Collectors.toList());
    }
	
	public FullEventDto getEvent(Long eventId) {
	    Event event = eventRepository.findById(eventId)
	            .orElseThrow(() -> new RuntimeException("Event not found"));
	    return toDetail(event);
	}

	public List<Event> filterEventsByStatus(String status) {
		return eventRepository.findByStatus(EventStatus.valueOf(status.toUpperCase()));
	}

	public List<Event> filterEventsByDate(LocalDate date) {
		return eventRepository.findByEventDate(date);
	}

	public Event updateEvent(Long id, UpdateEvent request) throws Exception {
		Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException("Event not found"));
		//checks the organizer who request to update is the one who created this event
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
	    Long loggedInUserId = userDetails.getId();

	    // Ownership check
	    if (!event.getOrganizer().getUserId().equals(loggedInUserId)) {
	        throw new Exception("You are not allowed to update this event");
	    }

		if (request.getEventName() != null) {
	        event.setEventName(request.getEventName());
	    }
	    if (request.getEventDate() != null) {
	        event.setEventDate(request.getEventDate());
	    }
	    if (request.getLocation() != null) {
	        event.setLocation(request.getLocation());
	    }
	    if (request.getCapacity() != null) {
	        event.setCapacity(request.getCapacity());
	    }
	    if (request.getDescription() != null) {
	        event.setDescription(request.getDescription());
	    }
	    if (request.getStatus() != null) {
	        event.setStatus(request.getStatus());
	    }

		return eventRepository.save(event);
	}

	public void deleteEvent(Long id) throws Exception {

		Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException("Event not found"));
		// event.setStatus(EventStatus.CANCELLED); // or ARCHIVED
		// checks the organizer who request to update is the one who created this event
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		Long loggedInUserId = userDetails.getId();

		// Ownership check
		if (!event.getOrganizer().getUserId().equals(loggedInUserId)) {
			throw new Exception("You are not allowed to update this event");
		}

		eventRepository.delete(event);
	}

//	public Event getEvent(Long eventId) {
//		// TODO Auto-generated method stub
//		
//		return eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not found"));
//	}
//	
//	
//	
//	
//	public FullEventDto getEventDetail(Long eventId) {
//	    Event event = eventRepository.findById(eventId)
//	            .orElseThrow(() -> new RuntimeException("Event not found"));
//	    return toDetail(event);
//	}
	
	
	
	
	
	
	public FullEventDto toSummary(Event event) {
		FullEventDto dto = new FullEventDto();
		dto.setEventId(event.getEventId());
        dto.setEventName(event.getEventName());
        dto.setEventDate(event.getEventDate());
        dto.setLocation(event.getLocation());
        dto.setCapacity(event.getCapacity());
        dto.setDescription(event.getDescription());
        dto.setStatus(event.getStatus().name());

        if (event.getOrganizer() != null && event.getOrganizer().getProfile() != null) {
            dto.setOrganizerName(event.getOrganizer().getProfile().getFullName());
        }
        // email & phone remain null here
        return dto;
    }

    // For /{id} — include all info
    public FullEventDto toDetail(Event event) {
    	FullEventDto dto = toSummary(event); // reuse the summary filling
        User organizer = event.getOrganizer();
        if (organizer != null) {
            dto.setOrganizerEmail(organizer.getEmail());
            if (organizer.getProfile() != null) {
                dto.setOrganizerPhone(organizer.getProfile().getPhoneNumber());
            }
        }
        return dto;
    }

}
