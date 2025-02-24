package com.hospital.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.dto.MeetingResponse;
import com.hospital.model.TabletInfo;
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
	            @RequestParam String patientRegNum,
	            @RequestParam String doctorNotes,
	            @RequestBody List<Map<String, Object>> tablets) {
	        try {
	            List<TabletInfo> savedTablets = tabletInfoService.saveTablets(doctorRegNum, patientRegNum,doctorNotes, tablets);
	            return ResponseEntity.status(HttpStatus.CREATED).body(savedTablets);
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save tablet data");
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
	    
	    
	    
	    @PostMapping("/generate")
	    public ResponseEntity<Resource> generatePdf(@RequestParam String patientName,
	                                                @RequestParam String patientEmail,
	                                                @RequestParam String phoneNumber,
	                                                @RequestParam String doctorEmail,
	                                                @RequestBody List<TabletInfo> tablets) {
	        String filePath = pdfService.generatePdf(patientName, patientEmail, phoneNumber, doctorEmail, tablets);
	        if (filePath == null) {
	            return ResponseEntity.internalServerError().build();
	        }

	        try {
	            Path path = Paths.get(filePath);
	            Resource resource = new UrlResource(path.toUri());

	            return ResponseEntity.ok()
	                    .contentType(MediaType.APPLICATION_PDF)
	                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + path.getFileName().toString())
	                    .body(resource);

	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.internalServerError().build();
	        }
	    }

}
