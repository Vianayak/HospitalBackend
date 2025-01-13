package com.hospital.service;

import jakarta.mail.MessagingException;

public interface EmailService {
	public void sendOTPEmail(String email, String otp);

	public void sendAppointmentConfirmation(String email, String firstName, String lastName, String date, String time,
			String doctorName, String specialty, int amount) throws MessagingException;
}
