package com.event.booking.management.controller;


import com.event.booking.management.DTO.*;
import com.event.booking.management.service.JwtService;
import com.event.booking.management.service.UserService;

import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
//@CrossOrigin(origins = "http://localhost:4200") // 
public class AuthController {

    @Autowired
    private UserService userService;
    
    @Autowired
	JwtService jwtService;
    
    @Autowired
	AuthenticationManager authenticationManager; 

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody RegisterRequest request) {
        String response = userService.registerUser(request);
        Map<String, String> body = new HashMap<>();
        body.put("message", response);
        return ResponseEntity.ok(body);
    }


    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest request) {

        
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		if (authentication.isAuthenticated()) {
			String response = jwtService.generateToken(request.getEmail());
			userService.loginUser(request);
            return ResponseEntity.ok(response);
		}
		return new ResponseEntity<>("failed",HttpStatus.UNAUTHORIZED);
        

    }
    
    @GetMapping("/{userId}/profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable Long userId) {
        UserProfileResponse response = userService.getUserProfile(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}/profile")
    public ResponseEntity<String> updateProfile(@PathVariable Long userId,
                                                @RequestBody UpdateProfileRequest request) {
        String response = userService.updateProfile(userId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<String> updatePassword(@PathVariable Long userId,
                                                 @RequestBody UpdatePasswordRequest request) {
        String response = userService.updatePassword(userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteProfile(@PathVariable Long userId) {
        String response = userService.deleteProfile(userId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/profile")
    public ResponseEntity<LoggedInUserResponse> getLoggedInUserProfile(Authentication authentication) {
        String email = authentication.getName(); // email extracted from JWT
        LoggedInUserResponse response = userService.getLoggedInUserProfile(email);
        return ResponseEntity.ok(response);
    }
}
