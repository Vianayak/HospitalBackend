package com.hospital.service;

import java.util.List;

import com.hospital.dto.UserDto;
import com.hospital.model.Users;

public interface UserService {

	public void sendOtp(String email);
	
	public void sendOtpToEmail(String email, String otp);
	
	public String generateOtp();

	public Users saveUser(UserDto dto);

	public List<Users> getAllUsers();
}
