package com.event.booking.management.service;

import com.event.booking.management.DTO.SeatTypeDTO;
import com.event.booking.management.DTO.SeatTypeResponseDTO;
import com.event.booking.management.exception.ResourceNotFoundException;
import com.event.booking.management.model.Event;
import com.event.booking.management.model.SeatType;
import com.event.booking.management.repository.EventRepository;
import com.event.booking.management.repository.SeatTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeatTypeServiceImpl implements SeatTypeService {

    @Autowired
    private SeatTypeRepository seatTypeRepository;

    @Autowired
    private EventRepository eventRepository;
    public SeatType addSeatType(Long eventId, SeatTypeDTO dto) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Event not found"));

        SeatType seatType = new SeatType();
        seatType.setSeatType(dto.getSeatType());
        seatType.setPrice(dto.getPrice());
        seatType.setTotalSeats(dto.getTotalSeats());
        seatType.setAvailableSeats(dto.getAvailableseats()); //  from DTO
        seatType.setEvent(event); // associate the event
      
        seatType.setEventName(dto.getEventName());

        return seatTypeRepository.save(seatType);
    }

    public List<SeatTypeResponseDTO> getSeatTypesByEvent(Long eventId) {
        List<SeatType> seatTypes = seatTypeRepository.findByEventEventId(eventId);
        return seatTypes.stream().map(seat -> new SeatTypeResponseDTO(
                seat.getId(),
                seat.getSeatType(),
                seat.getPrice(),
                seat.getTotalSeats(),
                seat.getAvailableSeats(),
                seat.getEvent().getEventName()
        )).collect(Collectors.toList());
    }



    public SeatType updateSeatType(Long id, SeatTypeDTO dto) {
        SeatType st = seatTypeRepository.findById(id).orElseThrow();
        st.setSeatType(dto.getSeatType());
        st.setPrice(dto.getPrice());
        st.setTotalSeats(dto.getTotalSeats());
        st.setAvailableSeats(dto.getAvailableseats());
        st.setEventName(dto.getEventName());
        return seatTypeRepository.save(st);
    }
    
    @Override
    public SeatType bookSeats(Long seatTypeId, int bookedSeats) {
        SeatType seatType = seatTypeRepository.findById(seatTypeId)
            .orElseThrow(() -> new ResourceNotFoundException("Seat type not found"));
        
        if (seatType.getAvailableSeats() < bookedSeats) {
            throw new IllegalArgumentException("Not enough available seats");
        }
        seatType.setBookedSeats(seatType.getBookedSeats() + bookedSeats);
        seatType.setAvailableSeats(seatType.getAvailableSeats() - bookedSeats);
        return seatTypeRepository.save(seatType);
    }


    public void deleteSeatType(Long id) {
        seatTypeRepository.deleteById(id);
    }
}
