package com.event.booking.management.controller;

import com.event.booking.management.DTO.SeatTypeDTO;
import com.event.booking.management.DTO.SeatTypeResponseDTO;
import com.event.booking.management.model.SeatType;
import com.event.booking.management.service.SeatTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seats")
public class SeatTypeController {

    @Autowired
    
    private SeatTypeService seatTypeService;

    @PreAuthorize("hasRole('ORGANIZER')")
    @PostMapping("/add/{eventId}")
    public SeatType add(@PathVariable Long eventId, @RequestBody SeatTypeDTO dto) {
        return seatTypeService.addSeatType(eventId, dto);
    }

    @GetMapping("/event/{eventId}")
    public List<SeatTypeResponseDTO> getByEvent(@PathVariable Long eventId) {
        return seatTypeService.getSeatTypesByEvent(eventId);
    }



    @PreAuthorize("hasRole('ORGANIZER')")
    @PutMapping("/{seatTypeId}")
    public SeatType update(@PathVariable Long seatTypeId, @RequestBody SeatTypeDTO dto) {
        return seatTypeService.updateSeatType(seatTypeId, dto);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/book/{seatTypeId}")
    public SeatType bookSeat(@PathVariable Long seatTypeId, @RequestParam int bookedSeats) {
        return seatTypeService.bookSeats(seatTypeId, bookedSeats);
    }

    
    @PreAuthorize("hasRole('ORGANIZER')")
    @DeleteMapping("/{seatTypeId}")
    public void delete(@PathVariable Long seatTypeId) {
        seatTypeService.deleteSeatType(seatTypeId);
    }
    
}
