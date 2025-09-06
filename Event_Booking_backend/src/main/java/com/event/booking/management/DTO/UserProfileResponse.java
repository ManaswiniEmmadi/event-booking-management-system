package com.event.booking.management.DTO;


import java.time.LocalDate;

public class UserProfileResponse {
    private String fullName;
    private String phoneNumber;
    private String gender;
    private LocalDate dateOfBirth;
    private String address;

    // Constructor
    public UserProfileResponse(String fullName, String phoneNumber, String gender, LocalDate dateOfBirth, String address) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    // Getters and setters
    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAddress() {
        return address;
    }
}
