package com.hospital.serviceImpl;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.hospital.repo.UserRepository;
import com.hospital.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository user;

	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void sendOtp(String email) {
		String otp = generateOtp();
		sendOtpToEmail(email, otp);
	}

	@Override
	public void sendOtpToEmail(String email, String otp) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("Your OTP Code");
		message.setText("Your OTP is: " + otp + ". Use this code to complete your registration.");

		mailSender.send(message);
	}

	@Override
	public String generateOtp() {
		Random random = new Random();
		int otp = 100000 + random.nextInt(900000);
		return String.valueOf(otp);
	}

}
