package com.event.booking.management.service;

import com.event.booking.management.DTO.SeatTypeDTO;
import com.event.booking.management.DTO.SeatTypeResponseDTO;
import com.event.booking.management.model.SeatType;
import java.util.List;

public interface SeatTypeService {
    SeatType addSeatType(Long eventId, SeatTypeDTO dto);
    List<SeatTypeResponseDTO> getSeatTypesByEvent(Long eventId);
    SeatType updateSeatType(Long seatTypeId, SeatTypeDTO dto);
    void deleteSeatType(Long seatTypeId);
	SeatType bookSeats(Long seatTypeId, int bookedSeats);
}
