package com.hospital.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.dto.UserDto;
import com.hospital.model.Users;
import com.hospital.service.JWTService;
import com.hospital.service.MyUserDetailsService;
import com.hospital.service.UserService;

@RestController
@RequestMapping("api/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JWTService jwtService;

	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@GetMapping("/")
	public String home() {
		return "Success";
	}
	@GetMapping("/all")
	public List<Users> getAllUsers() {
		List<Users> list = userService.getAllUsers();
		return list;
	}
	@PostMapping("/register")
	public String registerUser(@RequestBody UserDto dto) {
		Users user = userService.saveUser(dto);
		return "Registered Successfully";
		
	}
	
	@PostMapping("/login")
	public String loginUser(@RequestBody UserDto authRequest) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
			
		}catch(Exception e) {
			throw new Exception("Invalid Username or password",e);
		}
		
		final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authRequest.getEmail());
		final String jwtToken = jwtService.generateToken(userDetails.getUsername());
		return jwtToken;
	}
}
