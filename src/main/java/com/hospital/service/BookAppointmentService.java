package com.hospital.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.hospital.model.BookAppointment;
import com.razorpay.RazorpayException;

public interface BookAppointmentService {
	public BookAppointment initiate(BookAppointment initiateAppointment) throws RazorpayException;

	ResponseEntity<String> verifyPayment(Map<String, String> paymentDetails);
}
