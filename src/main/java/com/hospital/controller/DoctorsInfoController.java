package com.hospital.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.model.DateAndTimeInfo;
import com.hospital.model.DoctorsInfo;
import com.hospital.service.DateAndTimeInfoService;
import com.hospital.service.DoctorsInfoService;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "*")
public class DoctorsInfoController {
	
	@Autowired
	private DoctorsInfoService doctorsInfoService;
	
	@Autowired 
	private DateAndTimeInfoService dateInfoService;
	
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
	
	@GetMapping("/doctors-list")
    public ResponseEntity<List<DoctorsInfo>> getAllDoctors() {
        List<DoctorsInfo> doctors = doctorsInfoService.getDoctorsList();

        if (doctors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(doctors);
        }

        return ResponseEntity.status(HttpStatus.OK).body(doctors);
    }
	
	@GetMapping("/doctor-schedule")
	public ResponseEntity<List<DateAndTimeInfo>> getDoctorSchedule(@RequestParam int regNum) {
	    List<DateAndTimeInfo> doctorSchedule = dateInfoService.getDoctorSchedule(regNum);  // Adjusted to get list from service

	    if (doctorSchedule.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // Return NO_CONTENT if list is empty
	    }

	    return ResponseEntity.status(HttpStatus.OK).body(doctorSchedule);  // Return OK with the list of doctor schedules
	}

}
