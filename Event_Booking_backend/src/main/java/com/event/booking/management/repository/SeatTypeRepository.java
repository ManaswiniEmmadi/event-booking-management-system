package com.event.booking.management.repository;

import com.event.booking.management.model.SeatType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SeatTypeRepository extends JpaRepository<SeatType, Long> {
    List<SeatType> findByEventEventId(Long eventId);

	
}
