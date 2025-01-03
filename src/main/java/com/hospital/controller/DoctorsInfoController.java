package com.hospital.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.model.DoctorsInfo;
import com.hospital.service.DoctorsInfoService;

@RestController
@RequestMapping("/api/doctors")
public class DoctorsInfoController {
	
	@Autowired
	private DoctorsInfoService doctorsInfoService;
	
	@PostMapping("/save")
    public ResponseEntity<String> saveDoctor(@RequestBody DoctorsInfo doctor) {
        try {
            // Save the doctor using the service layer
            DoctorsInfo savedDoctor = doctorsInfoService.saveDoctor(doctor);

            // Return a success response
            return ResponseEntity.status(HttpStatus.CREATED)
                                 .body("Doctor saved successfully with ID: " + savedDoctor.getId());
        } catch (Exception e) {
            // Handle exceptions and return an error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to save the doctor: " + e.getMessage());
        }
    }
}
