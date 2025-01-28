package com.hospital.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.hospital.dto.AppointmentDto;
import com.hospital.dto.AppointmentStatsDTO;
import com.hospital.enums.DoctorStatus;
import com.hospital.model.BookAppointment;
import com.razorpay.RazorpayException;

import jakarta.mail.MessagingException;

public interface BookAppointmentService {
	public void initiate(AppointmentDto dto) throws RazorpayException, MessagingException;

	ResponseEntity<String> verifyPayment(Map<String, String> paymentDetails)  throws MessagingException;

	/* void updateAppointmentStatus(int appointmentId, DoctorStatus newStatus); */

//	AppointmentStatsDTO getStatsForDate(String date, String doctorRegNum);

	/*
	 * List<Map<String, Object>> getAppointmentsWithIssues(String date, String
	 * doctorRegNum);
	 * 
	 * List<Map<String, Object>> getAppointmentsWithIssuesForAccepted(String date,
	 * String doctorRegNum);
	 */

	Double getTodayEarnings(String doctorRegNum, String date);

	Double getTotalEarnings(String doctorRegNum);
}
