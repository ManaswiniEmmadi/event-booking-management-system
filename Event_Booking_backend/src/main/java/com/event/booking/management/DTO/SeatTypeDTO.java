package com.event.booking.management.DTO;

import jakarta.validation.constraints.NotNull;

public class SeatTypeDTO {
    private String seatType;
    private double price;
    private int totalSeats;
    private int availableseats;
    
    @NotNull
    private Long eventId;
    
    private String eventName;
    
	public int getAvailableseats() {
		return availableseats;
	}
	public void setAvailableseats(int availableseats) {
		this.availableseats = availableseats;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getSeatType() {
		return seatType;
	}
	public void setSeatType(String seatType) {
		this.seatType = seatType;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getTotalSeats() {
		return totalSeats;
	}
	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}

    // Getters & Setters
}
