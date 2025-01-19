package com.hospital.service;

import com.hospital.model.BookAppointment;
import com.hospital.model.DateAndTimeInfo;
import com.hospital.model.DoctorsInfo;
import com.hospital.model.MeetingDetails;

import jakarta.mail.MessagingException;

public interface EmailService {
	public void sendOTPEmail(String email, String otp);

	public void sendAppointmentConfirmation(String email, String firstName, String lastName, String date, String time,
			String doctorName, String specialty, int amount) throws MessagingException;

	void sendMeetingToPatient(MeetingDetails meet, BookAppointment app, DoctorsInfo doctor, DateAndTimeInfo info,
			String patientURL) throws MessagingException;

	void sendMeetingToDoctor(MeetingDetails meet, BookAppointment app, DoctorsInfo doctor, DateAndTimeInfo info,
			String doctorURL) throws MessagingException;
}
