package com.hospital.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.serviceImpl.OtpService;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;

@RestController
@RequestMapping("/api/otp")
@CrossOrigin(origins = "*")
public class OtpController {

	@Autowired
	private OtpService otpService;

	@PostMapping("/sendOtp")
    public ResponseEntity<String> sendOTP(@RequestParam String email) {
        // Validate email format
        if (!isValidEmail(email)) {
            return ResponseEntity.badRequest().body("Invalid email format.");
        }

        // Generate OTP and send to email
        String otp = otpService.generateOTP(email);
        otpService.sendOTPEmail(email, otp);

        return ResponseEntity.ok("OTP sent successfully to " + email);
    }

    // Method to validate email format using regex
    private boolean isValidEmail(String email) {
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();  // This will throw an exception if the email is invalid
            return true;
        } catch (AddressException ex) {
            return false;
        }
    }
	
	
    @PostMapping("/verifyOtp")
    public ResponseEntity<String> verifyOTP(@RequestParam String email, @RequestParam String otp) {
        // Validate the OTP using the service
        boolean isValid = otpService.validateOTP(email, otp);

        if (isValid) {
            return ResponseEntity.ok("OTP is correct!");
        } else {
            return ResponseEntity.status(400).body("Invalid or expired OTP.");
        }
    }

	
}
