package com.event.booking.management.service;


import com.event.booking.management.DTO.*;
import com.event.booking.management.exception.UserNotFoundException;
import com.event.booking.management.model.User;
import com.event.booking.management.model.UserProfile;
import com.event.booking.management.repository.UserProfileRepository;
import com.event.booking.management.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    public String registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword())); // Hash the password user.setPassword(request.getPassword());  
        user.setRole(User.Role.valueOf(request.getRole()));
        user.setCreatedAt(LocalDateTime.now());

        UserProfile profile = new UserProfile();
        profile.setFullName(request.getFullName());
        profile.setGender(request.getGender());
        profile.setDateOfBirth(request.getDateOfBirth());
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setAddress(request.getAddress());
        profile.setUser(user);

        user.setProfile(profile);  // link back to profile

        userRepository.save(user);  // cascades and saves profile as well

        return "User registered successfully";
    }

    

    public String loginUser(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail());

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        return "Login successful";
    }

    public String updateProfile(Long userId, UpdateProfileRequest profileRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        UserProfile profile = userProfileRepository.findByUser(user);

        if (profile == null) {
            profile = new UserProfile();
            profile.setUser(user);
        }

        profile.setFullName(profileRequest.getFullName());
        profile.setPhoneNumber(profileRequest.getPhoneNumber());
        profile.setGender(profileRequest.getGender());
        profile.setDateOfBirth(profileRequest.getDateOfBirth());
        profile.setAddress(profileRequest.getAddress());

        userProfileRepository.save(profile);
        return "Profile updated successfully";
    }

    public String updatePassword(Long userId, UpdatePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!user.getPassword().equals(request.getOldPassword())) {
            throw new RuntimeException("Incorrect old password");
        }

        user.setPassword(request.getNewPassword());
        userRepository.save(user);
        return "Password updated successfully";
    }

    public String deleteProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        UserProfile profile = userProfileRepository.findByUser(user);
        if (profile != null) {
            userProfileRepository.delete(profile);
        }

        userRepository.delete(user);
        return "User profile and account deleted successfully";
    }
    
    public UserProfileResponse getUserProfile(Long userId) {
        UserProfile profile = userProfileRepository.findByUserUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("Profile not found"));

        return new UserProfileResponse(
                profile.getFullName(),
                profile.getPhoneNumber(),
                profile.getGender(),
                profile.getDateOfBirth(),
                profile.getAddress()
        );
    }
    
    public LoggedInUserResponse getLoggedInUserProfile(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        return new LoggedInUserResponse(
                user.getProfile().getFullName(),
                user.getProfile().getPhoneNumber(),
                user.getProfile().getGender(),
                user.getProfile().getDateOfBirth(),
                user.getProfile().getAddress(),
                user.getEmail(),
                user.getRole().name()
        );
    }

}
