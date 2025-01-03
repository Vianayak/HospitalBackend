package com.hospital.serviceImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class OtpService {
	private final Map<String, String> otpStorage = new HashMap<>(); 
	private final int otpValiditySeconds = 300; 

	@Autowired
	private JavaMailSender mailSender;

	
	public String generateOTP(String identifier) {
		String otp = String.format("%04d", new Random().nextInt(10000)); 
		otpStorage.put(identifier, otp);

		
		new Thread(() -> {
			try {
				Thread.sleep(otpValiditySeconds * 1000L);
				otpStorage.remove(identifier);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}).start();

		return otp;
	}

	
	public void sendOTPEmail(String email, String otp) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("Your OTP Code");
		message.setText("Dear User,\n\nYour OTP is: " + otp + "\n\nThis OTP is valid for 5 minutes.");

		mailSender.send(message);
	}

	
	public boolean validateOTP(String identifier, String otp) {
		return otp.equals(otpStorage.get(identifier));
	}
}
