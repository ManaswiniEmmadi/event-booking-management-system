package com.event.booking.management.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
public class SeatType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String seatType;
    private double price;
    private int totalSeats;
    private int availableSeats;
    private String eventName;
    
    private int bookedSeats;

    @ManyToOne
    @JoinColumn(name = "event_id")
    @JsonBackReference
    private Event event;

    public SeatType() {}

    public SeatType(String seatType, double price, int totalSeats, int availableseats,String eventName,Event event, int bookedSeats) {
        this.seatType = seatType;
        this.price = price;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.event = event;
        this.bookedSeats = bookedSeats;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public int getAvailableSeats() {
		return availableSeats;
	}

	public void setAvailableSeats(int availableSeats) {
		this.availableSeats = availableSeats;
	}
	

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public int getBookedSeats() {
		// TODO Auto-generated method stub
		return bookedSeats;
	}
	
	public void setBookedSeats(int bookedSeats) {
		this.bookedSeats = bookedSeats;
	}

    // Getters & Setters
}
