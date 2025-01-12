package com.hospital.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.dto.AppointmentDto;
import com.hospital.service.BookAppointmentService;
import com.razorpay.RazorpayException;

@RestController
@RequestMapping("/api/book-appointment")
@CrossOrigin(origins = "*")
public class BookAppointmentController {
	@Autowired
	private BookAppointmentService bookApp;

	@PostMapping(value = "/initiate")
	public ResponseEntity<Map<String, Object>> createOrder(@RequestBody AppointmentDto dto) throws RazorpayException {
		bookApp.initiate(dto);
		Map<String, Object> response = new HashMap<>();
	    response.put("razorpayOrderId", dto.getRazorpayOrderId());
	    response.put("amount", dto.getAmount());
	    response.put("currency", "INR");
	    response.put("status", dto.getOrderStatus());

	    return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	
	
	@PostMapping("/verify-payment")
    public ResponseEntity<String> verifyPayment(@RequestBody Map<String, String> paymentDetails) {
        return bookApp.verifyPayment(paymentDetails);
    }
	


}
