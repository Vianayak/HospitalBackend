package com.hospital.controller;

import java.util.Properties;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.service.EmailService;
import com.hospital.serviceImpl.OtpService;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;

@RestController
@RequestMapping("/api/otp")
@CrossOrigin(origins = "*")
public class OtpController {

	@Autowired
	private OtpService otpService;
	
	@Autowired
	private EmailService emailService;

	@PostMapping("/sendOtp")
    public ResponseEntity<String> sendOTP(@RequestParam String email) {
        // Validate email format
        if (!isValidEmail(email)) {
            return ResponseEntity.badRequest().body("Invalid email format.");
        }

        // Generate OTP and send to email
        String otp = otpService.generateOTP(email);
        emailService.sendOTPEmail(email, otp);

        return ResponseEntity.ok("OTP sent successfully to " + email);
    }

    // Method to validate email format using regex
	public boolean isValidEmail(String email) {
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
            String domain = email.substring(email.indexOf("@") + 1);
            return hasMXRecords(domain);
        } catch (AddressException ex) {
            return false;
        }
    }

    private boolean hasMXRecords(String domain) {
        try {
            Properties env = new Properties();
            env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            DirContext dirContext = new InitialDirContext(env);
            Attributes attributes = dirContext.getAttributes(domain, new String[]{"MX"});
            return attributes.get("MX") != null;
        } catch (NamingException ex) {
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
