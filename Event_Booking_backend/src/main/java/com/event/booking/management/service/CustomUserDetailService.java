package com.event.booking.management.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.event.booking.management.model.User;
import com.event.booking.management.model.UserDetailsImpl;
import com.event.booking.management.repository.UserRepository;


@Service//used in SecurityConfig
public class CustomUserDetailService implements UserDetailsService{
	
	@Autowired
	UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user=userRepo.findByEmail(username);
		if(user==null) {
			throw new UsernameNotFoundException(username+" not found");
		}
		return new UserDetailsImpl(user);
	}

}
