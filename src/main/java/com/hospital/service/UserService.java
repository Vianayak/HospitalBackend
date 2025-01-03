package com.hospital.service;

public interface UserService {

	public void sendOtp(String email);
	
	public void sendOtpToEmail(String email, String otp);
	
	public String generateOtp();
}
