package com.hospital.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.hospital.dto.AppointmentDto;
import com.razorpay.RazorpayException;

import jakarta.mail.MessagingException;

public interface BookAppointmentService {
	public void initiate(AppointmentDto dto) throws RazorpayException, MessagingException;

	ResponseEntity<String> verifyPayment(Map<String, String> paymentDetails)  throws MessagingException;
}
