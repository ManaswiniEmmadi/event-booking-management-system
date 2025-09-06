package com.event.booking.management.service;

import com.event.booking.management.DTO.BookingRequestDTO;
import com.event.booking.management.DTO.BookingResponseDTO;
import com.event.booking.management.exception.BookingNotFoundException;
import com.event.booking.management.exception.EventNotFoundException;
import com.event.booking.management.exception.UserNotFoundException;
import com.event.booking.management.model.*;
import com.event.booking.management.repository.BookingRepository;
import com.event.booking.management.repository.EventRepository;
import com.event.booking.management.repository.SeatTypeRepository;
import com.event.booking.management.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private SeatTypeRepository seatTypeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SeatTypeService seatTypeService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private User user;
    private Event event;
    private SeatType seatType;
    private Booking booking;
    private BookingRequestDTO requestDTO;

    @BeforeEach
    void setup() {
        // Mock SecurityContextHolder for logged-in user
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        user = new User();
        user.setUserId(1L);
        user.setEmail("test@test.com");
        user.setPassword("pass");
        user.setRole(User.Role.USER);

        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        event = new Event();
        event.setEventId(10L);
        event.setEventName("Music Fest");
        event.setEventDate(LocalDate.of(2030, 1, 1));
        event.setLocation("Hyderabad");
        event.setCapacity(100);

        seatType = new SeatType();
        seatType.setId(5L);
        seatType.setSeatType("VIP");
        seatType.setPrice(500.0);
        seatType.setTotalSeats(50);
        seatType.setAvailableSeats(40);
        seatType.setEvent(event);

        booking = new Booking();
        booking.setBookingId(100L);
        booking.setUserName("John Doe");
        booking.setMobile("9876543210");
        booking.setEmail("john@example.com");
        booking.setSeatType(seatType);
        booking.setNumberOfSeats(2);
        booking.setEventId(event.getEventId());
        booking.setEvent(event);
        booking.setUser(user);
        booking.setStatus("CONFIRMED");

        requestDTO = new BookingRequestDTO();
        requestDTO.setUserName("John Doe");
        requestDTO.setMobile("9876543210");
        requestDTO.setEmail("john@example.com");
        requestDTO.setSeatTypeId(5L);
        requestDTO.setNumberOfSeats(2);
        requestDTO.setEventId(10L);
        requestDTO.setStatus("CONFIRMED");
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    //createBooking 
    @Test
    void createBooking_success() {
        when(eventRepository.findById(10L)).thenReturn(Optional.of(event));
        when(seatTypeRepository.findById(5L)).thenReturn(Optional.of(seatType));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(seatTypeService.bookSeats(5L, 2)).thenReturn(seatType);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingResponseDTO resp = bookingService.createBooking(requestDTO);

        assertNotNull(resp);
        assertEquals("John Doe", resp.getUserName());
        assertEquals("VIP", resp.getSeatType());
        assertEquals(500.0, resp.getPrice());
        assertEquals(2, resp.getNumberOfSeats());
        assertEquals(10L, resp.getEventId());
        assertEquals("CONFIRMED", resp.getStatus());
        assertEquals("Music Fest", resp.getEventName());
        verify(bookingRepository, times(1)).save(any(Booking.class));
        verify(seatTypeService, times(1)).bookSeats(5L, 2);
    }

    @Test
    void createBooking_eventNotFound_throws() {
        when(eventRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> bookingService.createBooking(requestDTO));
        verifyNoInteractions(seatTypeRepository, userRepository, seatTypeService, bookingRepository);
    }

    @Test
    void createBooking_seatTypeNotFound_throwsRuntime() {
        when(eventRepository.findById(10L)).thenReturn(Optional.of(event));
        when(seatTypeRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> bookingService.createBooking(requestDTO));
        verifyNoInteractions(userRepository, seatTypeService, bookingRepository);
    }

    @Test
    void createBooking_userNotFound_throws() {
        when(eventRepository.findById(10L)).thenReturn(Optional.of(event));
        when(seatTypeRepository.findById(5L)).thenReturn(Optional.of(seatType));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> bookingService.createBooking(requestDTO));
        verifyNoInteractions(seatTypeService, bookingRepository);
    }

    //getAllBookings
    @Test
    void getAllBookings_returnsMappedList() {
        when(bookingRepository.findAll()).thenReturn(Collections.singletonList(booking));

        List<BookingResponseDTO> list = bookingService.getAllBookings();

        assertEquals(1, list.size());
        assertEquals("John Doe", list.get(0).getUserName());
        assertEquals("VIP", list.get(0).getSeatType());
    }

    //getBookingsForLoggedInUser
    @Test
    void getBookingsForLoggedInUser_returnsOnlyCurrentUser() {
        when(bookingRepository.findByUser_UserId(1L)).thenReturn(Collections.singletonList(booking));

        List<BookingResponseDTO> list = bookingService.getBookingsForLoggedInUser();

        assertEquals(1, list.size());
        assertEquals("John Doe", list.get(0).getUserName());
        verify(bookingRepository).findByUser_UserId(1L);
    }

    //getBookingsByEventId 
    @Test
    void getBookingsByEventId_returnsMappedList() {
        when(bookingRepository.findByEventId(10L)).thenReturn(Collections.singletonList(booking));

        List<BookingResponseDTO> list = bookingService.getBookingsByEventId(10L);

        assertEquals(1, list.size());
        assertEquals(10L, list.get(0).getEventId());
        assertEquals("Music Fest", list.get(0).getEventName());
    }

    //getBookingById
    @Test
    void getBookingById_found() {
        when(bookingRepository.findById(100L)).thenReturn(Optional.of(booking));

        BookingResponseDTO resp = bookingService.getBookingById(100L);

        assertEquals("John Doe", resp.getUserName());
        assertEquals("VIP", resp.getSeatType());
    }

    @Test
    void getBookingById_notFound_throws() {
        when(bookingRepository.findById(200L)).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class, () -> bookingService.getBookingById(200L));
    }

    //getBookingsByStatus
    @Test
    void getBookingsByStatus_returnsMapped() {
        when(bookingRepository.findByStatusIgnoreCase("CONFIRMED"))
                .thenReturn(Collections.singletonList(booking));

        List<BookingResponseDTO> list = bookingService.getBookingsByStatus("CONFIRMED");

        assertEquals(1, list.size());
        assertEquals("CONFIRMED", list.get(0).getStatus());
        assertEquals("VIP", list.get(0).getSeatType());
    }

    //updateBooking 
    @Test
    void updateBooking_success() {
        
        when(bookingRepository.findById(100L)).thenReturn(Optional.of(booking));
        when(eventRepository.findById(10L)).thenReturn(Optional.of(event));
        when(seatTypeRepository.findById(5L)).thenReturn(Optional.of(seatType));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingResponseDTO resp = bookingService.updateBooking(100L, requestDTO);

        assertNotNull(resp);
        assertEquals("John Doe", resp.getUserName());
        assertEquals("VIP", resp.getSeatType());
        assertEquals(2, resp.getNumberOfSeats());
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void updateBooking_bookingNotFound_throws() {
        when(bookingRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class,
                () -> bookingService.updateBooking(999L, requestDTO));
        verifyNoInteractions(eventRepository, seatTypeRepository);
    }

    @Test
    void updateBooking_eventNotFound_throws() {
        when(bookingRepository.findById(100L)).thenReturn(Optional.of(booking));
        when(eventRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class,
                () -> bookingService.updateBooking(100L, requestDTO));
        verifyNoInteractions(seatTypeRepository);
    }

    //deleteBookingById 
    @Test
    void deleteBookingById_success() {
        when(bookingRepository.existsById(100L)).thenReturn(true);

        bookingService.deleteBookingById(100L);

        verify(bookingRepository, times(1)).deleteById(100L);
    }

    @Test
    void deleteBookingById_notFound_throws() {
        when(bookingRepository.existsById(200L)).thenReturn(false);

        assertThrows(BookingNotFoundException.class, () -> bookingService.deleteBookingById(200L));
        verify(bookingRepository, never()).deleteById(anyLong());
    }

    //updateBookingStatus 
    @Test
    void updateBookingStatus_updatesAndReturnsDTO() {
        when(bookingRepository.findById(100L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingResponseDTO resp = bookingService.updateBookingStatus(100L, "CANCELLED");

        assertEquals("CANCELLED", resp.getStatus());
        verify(bookingRepository).save(any(Booking.class));
    }
}
