package com.hospital.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.dto.AppointmentDto;
import com.hospital.dto.AppointmentStatsDTO;
import com.hospital.enums.DoctorStatus;
import com.hospital.model.BookAppointment;
import com.hospital.model.Issue;
import com.hospital.service.BookAppointmentService;
import com.hospital.service.IssueService;
import com.razorpay.RazorpayException;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/api/book-appointment")
@CrossOrigin(origins = "*")
public class BookAppointmentController {
	@Autowired
	private BookAppointmentService bookApp;
	
	     @Autowired
	    private IssueService issueService;
	


	@PostMapping(value = "/initiate")
	public ResponseEntity<Map<String, Object>> createOrder(@RequestBody AppointmentDto dto) throws RazorpayException, MessagingException {
		bookApp.initiate(dto);
		Map<String, Object> response = new HashMap<>();
	    response.put("razorpayOrderId", dto.getRazorpayOrderId());
	    response.put("amount", dto.getAmount());
	    response.put("currency", "INR");
	    response.put("status", dto.getOrderStatus());

	    return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	
	
	@PostMapping("/verify-payment")
    public ResponseEntity<String> verifyPayment(@RequestBody Map<String, String> paymentDetails) throws MessagingException {
        return bookApp.verifyPayment(paymentDetails);
    }
	
	@PutMapping("/{id}/")
    public ResponseEntity<String> updateAppointmentStatus(
            @PathVariable int id,
            @RequestParam("status") DoctorStatus status) {
        
        // Update the status
		bookApp.updateAppointmentStatus(id, status);

        return ResponseEntity.ok("Appointment status updated to " + status);
    }
	
	
	@GetMapping("/stats")
    public ResponseEntity<AppointmentStatsDTO> getStats(@RequestParam("date") String date,@RequestParam("doctorRegNum") String regNum) {
        AppointmentStatsDTO stats = bookApp.getStatsForDate(date,regNum);
        return ResponseEntity.ok(stats);
    }
	
	
	@GetMapping("/appointments-with-issues")
    public List<Map<String, Object>> getAppointmentsWithIssues(
            @RequestParam String date,
            @RequestParam String doctorRegNum) {

        return bookApp.getAppointmentsWithIssues(date, doctorRegNum);
    }
	
	
	@GetMapping("/appointments-with-issues-accepted")
    public List<Map<String, Object>> getAppointmentsWithIssuesForAccepted(
            @RequestParam String date,
            @RequestParam String doctorRegNum) {

        return bookApp.getAppointmentsWithIssuesForAccepted(date, doctorRegNum);
    }
}
