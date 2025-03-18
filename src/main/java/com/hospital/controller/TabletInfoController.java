package com.hospital.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.dto.MeetingResponse;
import com.hospital.dto.PrescriptionDetailsDto;
import com.hospital.service.TabletInfoService;
import com.hospital.serviceImpl.PdfService;

	@RestController
	@RequestMapping("/api/tablets")
	@CrossOrigin(origins = "*")
	public class TabletInfoController {

		@Autowired
		private PdfService pdfService;
		
	    @Autowired
	    private TabletInfoService tabletInfoService;

	    @PostMapping("/saveTablets")
	    public ResponseEntity<?> saveTablets(
	            @RequestParam String doctorRegNum,
	            @RequestParam String patientEmail,
	            @RequestParam String doctorNotes,
	            @RequestParam String doctorFeedback,
	            @RequestBody List<Map<String, Object>> tablets) {
	        try {
	            tabletInfoService.saveTablets(doctorRegNum, patientEmail,doctorNotes,doctorFeedback, tablets);
	            return ResponseEntity.status(HttpStatus.CREATED).body("saved");
	        } catch (Exception e) {
	            return ResponseEntity.status(500).build();
	        }
	    }
	    
	    @GetMapping("/patientDetails")
	    public ResponseEntity<List<MeetingResponse>> getDoctorMeetings(
	            @RequestParam String email, 
	            @RequestParam String date) {

	        List<MeetingResponse> meetings = tabletInfoService.getDoctorMeetings(date, email);

	        if (meetings.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	        }

	        return ResponseEntity.ok(meetings);
	    }
	    
	    
	    
	    @GetMapping("/prescriptionHistory/{docRegNum}")
	    public ResponseEntity<List<PrescriptionDetailsDto>> getDoctorPrecriptions(@PathVariable String docRegNum) {
	    	
	    	

	    	List<PrescriptionDetailsDto> lst=tabletInfoService.getDoctorPrescriptions(docRegNum);
	    	
	    	if (lst.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	        }

	        return ResponseEntity.ok(lst);
	    }
	    
	    

}
