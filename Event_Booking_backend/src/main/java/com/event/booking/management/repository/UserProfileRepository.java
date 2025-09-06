package com.event.booking.management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.event.booking.management.model.User;
import com.event.booking.management.model.UserProfile;
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    // Get profile by user
    UserProfile findByUser(User user);
    Optional<UserProfile> findByUserUserId(Long userId);
    
  

}


