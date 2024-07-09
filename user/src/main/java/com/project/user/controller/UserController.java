package com.project.user.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.user.Entity.User;
import com.project.user.dto.AuthResponse;
import com.project.user.dto.AuthenticationRequest;
import com.project.user.enums.LevelOfUser;
import com.project.user.repo.UserRepository;
import com.project.user.util.JwtUtil;

@RestController
@RequestMapping(path = "/")
public class UserController {
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired 
	private UserRepository userRepository ;
	
	@Autowired
	private InMemoryUserDetailsManager userDetailsManager;
	
	@GetMapping(path = "/")
	public String hello() {
//		System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
		return "Hello World";
	}
	
	@GetMapping(path = "/authenticate")
	public String hello2() {
		return "Hello World 2 ";
	}
	
	@PostMapping(path = "/authenticate")
	public AuthResponse authenticate(@RequestBody AuthenticationRequest request) {
		UsernamePasswordAuthenticationToken authentication = null;
		try {
			authentication = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
			authenticationManager.authenticate(authentication); 
		} catch(Exception ex) {
			System.out.println(ex.getMessage());
			return new AuthResponse(new User(), "Invalid username/password");
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);
//		System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
//		return jwtUtil.generateToken(request.getUsername());
		User user = userRepository.findByUsername(request.getUsername());
		return new AuthResponse(user, jwtUtil.generateToken(request.getUsername()));
	}
	
	@PostMapping(path = "/authenticate/register")
	public String register(@RequestBody AuthenticationRequest request) {
		
		User existingUser = userRepository.findByUsername(request.getUsername());
		
		if(existingUser == null) {
			String pswd = new BCryptPasswordEncoder().encode(request.getPassword());
			User user = new User(request.getUsername(), pswd, 0, LevelOfUser.LOW.name());
			userRepository.save(user);
			UserDetails userDetails = new org.springframework.security.core.userdetails.User(request.getUsername(), pswd, new ArrayList<>());
			userDetailsManager.createUser(userDetails);
			return "Success";
		}
		
		return "Error";
	}
	
	@PutMapping(path = "/add-rating")
	public User newRating() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByUsername(username);
		User userToUpdate = userRepository.getReferenceById(user.getId());
		long ratings = userToUpdate.getNumOfRatings()+1;
		userToUpdate.setNumOfRatings(ratings);
		
		if(!userToUpdate.getLevelOfUser().equals(LevelOfUser.HIGH.name())) {
			if(ratings % 10 == 0) {
				userToUpdate.setLevelOfUser(LevelOfUser.findLevelOfUser( (int) ratings ).name());
			}
		}
		
//		userRepository.deleteById(user.getId());
		
		userRepository.save(userToUpdate);
		
		return user;
	}
	
}
