package com.hospital.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.serviceImpl.OtpService;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

	@Autowired
	private OtpService otpService;

	@PostMapping("/sendOtp")
	public ResponseEntity<String> sendOTP(@RequestParam String email) {
		
		String otp = otpService.generateOTP(email);

	
		otpService.sendOTPEmail(email, otp);

	
		return ResponseEntity.ok("OTP sent successfully to " + email);
	}
	
	
	@PostMapping("/verifyOtp")
    public ResponseEntity<String> verifyOTP(@RequestParam String email, @RequestParam String otp) {
        if (otpService.validateOTP(email, otp)) {
            return ResponseEntity.ok("OTP is correct!");
        } else {
            return ResponseEntity.status(400).body("Invalid or expired OTP.");
        }
    }
	
}
