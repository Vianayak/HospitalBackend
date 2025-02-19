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


	private Map<String, OtpData> otpStore = new HashMap<>();

	public String generateOTP(String email) {
		// Generate a 6-digit OTP
		String otp = String.format("%06d", new Random().nextInt(1000000));

		// Set expiration time to 5 minutes from now
		long expirationTime = System.currentTimeMillis() + 5 * 60 * 1000; // 5 minutes in milliseconds

		// Store the OTP and expiration time
		otpStore.put(email, new OtpData(otp, expirationTime));

		return otp;
	}

	public boolean validateOTP(String email, String otp) {
		// Retrieve the OTP data for the given email
		OtpData otpData = otpStore.get(email);

		if (otpData != null) {
			// Check if the OTP matches and if it hasn't expired
			if (otpData.getOtp().equals(otp) && System.currentTimeMillis() <= otpData.getExpirationTime()) {
				return true;
			}
		}
		return false;
	}

	// Helper class to store OTP and expiration time
	public static class OtpData {
		private String otp;
		private long expirationTime;

		public OtpData(String otp, long expirationTime) {
			this.otp = otp;
			this.expirationTime = expirationTime;
		}

		public String getOtp() {
			return otp;
		}

		public long getExpirationTime() {
			return expirationTime;
		}
	}

	
	
	
	public String generateForgotOTP(String email) {
		// Generate a 6-digit OTP
		
		
		String otp = String.format("%06d", new Random().nextInt(1000000));

		// Set expiration time to 5 minutes from now
		long expirationTime = System.currentTimeMillis() + 5 * 60 * 1000; // 5 minutes in milliseconds

		// Store the OTP and expiration time
		otpStore.put(email, new OtpData(otp, expirationTime));

		return otp;
	}
	
}
