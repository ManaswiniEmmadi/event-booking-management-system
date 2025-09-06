package com.event.booking.management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.event.booking.management.DTO.BookingRequestDTO;
import com.event.booking.management.DTO.BookingResponseDTO;
import com.event.booking.management.exception.BookingNotFoundException;
import com.event.booking.management.exception.EventNotFoundException;
import com.event.booking.management.exception.UserNotFoundException;
import com.event.booking.management.model.Booking;
import com.event.booking.management.model.Event;
import com.event.booking.management.model.SeatType;
import com.event.booking.management.model.User;
import com.event.booking.management.model.UserDetailsImpl;
import com.event.booking.management.repository.BookingRepository;
import com.event.booking.management.repository.EventRepository;
import com.event.booking.management.repository.SeatTypeRepository;
import com.event.booking.management.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SeatTypeRepository seatTypeRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SeatTypeService seatTypeService;

    private BookingResponseDTO toDTO(Booking booking) {
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setBookingId(booking.getBookingId());
        dto.setUserName(booking.getUserName());
        dto.setMobile(booking.getMobile());
        dto.setEmail(booking.getEmail());
        dto.setSeatType(booking.getSeatType() != null ? booking.getSeatType().getSeatType() : null);
        dto.setPrice(booking.getSeatType() != null ? booking.getSeatType().getPrice() : null); 
        dto.setNumberOfSeats(booking.getNumberOfSeats());
        dto.setEventId(booking.getEventId());
        dto.setStatus(booking.getStatus());
        
        Event event = booking.getEvent();
        if (event != null) {
            dto.setEventName(event.getEventName());
            dto.setEventDate(event.getEventDate() != null ? event.getEventDate().toString() : null);
            dto.setLocation(event.getLocation());
        }
        
        return dto;
    }


    public BookingResponseDTO createBooking(BookingRequestDTO dto) {

        //1 Get event
        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        //2 Get seat type
        SeatType seatType = seatTypeRepository.findById(dto.getSeatTypeId())
                .orElseThrow(() -> new RuntimeException("SeatType not found"));

        //3 Get logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long loggedInUserId = userDetails.getId();

        User bookingUser = userRepository.findById(loggedInUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        //4 Create booking
        Booking booking = new Booking();
        booking.setUserName(dto.getUserName());
        booking.setMobile(dto.getMobile());
        booking.setEmail(dto.getEmail());
        booking.setSeatType(seatType);
        booking.setNumberOfSeats(dto.getNumberOfSeats());
        booking.setEventId(event.getEventId());
        booking.setEvent(event);
        booking.setStatus(dto.getStatus());

        //5 Link user
        booking.setUser(bookingUser);
        
        //6 Update available seats
        seatTypeService.bookSeats(dto.getSeatTypeId(), dto.getNumberOfSeats());

        //7 Save and return
        return toDTO(bookingRepository.save(booking));
    }


    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<BookingResponseDTO> getBookingsForLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long loggedInUserId = userDetails.getId();

        List<Booking> bookings = bookingRepository.findByUser_UserId(loggedInUserId);
        return bookings.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    public List<BookingResponseDTO> getBookingsByEventId(Long eventId) {
        return bookingRepository.findByEventId(eventId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public BookingResponseDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking with ID " + id + " not found"));
        return toDTO(booking);
    }

    public List<BookingResponseDTO> getBookingsByStatus(String status) {
        return bookingRepository.findByStatusIgnoreCase(status)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public BookingResponseDTO updateBooking(Long id, BookingRequestDTO dto) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking with ID " + id + " not found"));

        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        SeatType seatType = seatTypeRepository.findById(dto.getSeatTypeId())
                .orElseThrow(() -> new RuntimeException("SeatType not found"));

        booking.setUserName(dto.getUserName());
        booking.setMobile(dto.getMobile());
        booking.setEmail(dto.getEmail());
        booking.setSeatType(seatType);
        booking.setNumberOfSeats(dto.getNumberOfSeats());
        booking.setEventId(event.getEventId());
        booking.setEvent(event);
        booking.setStatus(dto.getStatus());

        return toDTO(bookingRepository.save(booking));
    }

    public void deleteBookingById(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new BookingNotFoundException("Cannot delete. Booking with ID " + id + " not found");
        }
        bookingRepository.deleteById(id);
    }

    
    
    public BookingResponseDTO updateBookingStatus(Long bookingId, String status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        booking.setStatus(status);
        bookingRepository.save(booking);

        return toDTO(booking);
    }


	public List<BookingResponseDTO> getBookingsForEvent(Long eventId) {
		List<Booking> bookings = bookingRepository.findByEventId(eventId);
	    return bookings.stream()
	                   .map(this::toDTO)
	                   .collect(Collectors.toList());
	}
}
