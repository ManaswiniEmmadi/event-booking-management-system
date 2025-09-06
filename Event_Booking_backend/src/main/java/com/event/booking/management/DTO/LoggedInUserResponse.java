
package com.event.booking.management.DTO;

import java.time.LocalDate;

public class LoggedInUserResponse {
    private String fullName;
    private String phoneNumber;
    private String gender;
    private LocalDate dateOfBirth;
    private String address;
    private String email;
    private String role;

    public LoggedInUserResponse(String fullName, String phoneNumber, String gender,
                                LocalDate dateOfBirth, String address, String email, String role) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.email = email;
        this.role = role;
    }

    // getters only (immutable DTO)
    public String getFullName() { return fullName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getGender() { return gender; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getAddress() { return address; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
}
