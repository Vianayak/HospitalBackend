package com.hospital.serviceImpl;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hospital.dto.UserDto;
import com.hospital.model.Users;
import com.hospital.repo.UserRepo;
import com.hospital.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private PasswordEncoder encoder;

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

	
	//srihari
	@Override
	public Users saveUser(UserDto dto) {
		Users users = new Users();
		users.setId(dto.getId());
		users.setEmail(dto.getEmail());
		users.setFirstName(dto.getFirstName());
		users.setLastName(dto.getLastName());
		users.setMobileNumber(dto.getMobileNumber());
		users.setPassword(encoder.encode(dto.getPassword()));//Encoding the passowrd
		users.setRegistrationNumber(dto.getRegistrationNumber());
		users.setRole(dto.getRole());
		return userRepo.save(users);
	}

	@Override
	public List<Users> getAllUsers() {
		return userRepo.findAll();
	}

}
