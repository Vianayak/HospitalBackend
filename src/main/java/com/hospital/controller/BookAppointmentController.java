package com.hospital.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.model.BookAppointment;
import com.hospital.service.BookAppointmentService;
import com.razorpay.RazorpayException;

@RestController
@RequestMapping("/api/book-appointment")
@CrossOrigin(origins = "*")
public class BookAppointmentController {
	@Autowired
	private BookAppointmentService bookApp;

	@PostMapping(value = "/initiate")
	public ResponseEntity<BookAppointment> createOrder(@RequestBody BookAppointment initiateAppointment)
			throws RazorpayException {
		BookAppointment razorpayOrder = bookApp.initiate(initiateAppointment);
		return new ResponseEntity<BookAppointment>(razorpayOrder, HttpStatus.CREATED);
	}
	
	@PostMapping("/paymentCallback")
    public ResponseEntity<?> paymentCallback(@RequestBody Map<String, String> response) {
		bookApp.updateStatus(response);
        return ResponseEntity.ok().body("Payment successful");
    }

}
