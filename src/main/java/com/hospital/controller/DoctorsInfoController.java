package com.hospital.controller;

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
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.dto.DoctorScheduleDTO;
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
    public ResponseEntity<String> saveDoctor(@RequestParam("doctor") String doctorJson,
            @RequestParam("file") MultipartFile file) {
        try {
        	
        	ObjectMapper objectMapper = new ObjectMapper();
            DoctorsInfo doctor = objectMapper.readValue(doctorJson, DoctorsInfo.class);
            
            // Save the doctor using the service layer
            DoctorsInfo savedDoctor = doctorsInfoService.saveDoctor(doctor,file);

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
	
	/*
	 * @GetMapping("/doctor-schedule") public ResponseEntity<List<DateAndTimeInfo>>
	 * getDoctorSchedule(@RequestParam String regNum) { List<DateAndTimeInfo>
	 * doctorSchedule = dateInfoService.getDoctorSchedule(regNum); // Adjusted to
	 * get list from service
	 * 
	 * if (doctorSchedule.isEmpty()) { return
	 * ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // Return NO_CONTENT if
	 * list is empty }
	 * 
	 * return ResponseEntity.status(HttpStatus.OK).body(doctorSchedule); // Return
	 * OK with the list of doctor schedules }
	 */
	
	@GetMapping("/{doctorId}")
    public DoctorsInfo getDoctorById(@PathVariable int doctorId) {
        return doctorsInfoService.getDoctorById(doctorId);
    }

	
	@PostMapping("/doctor-details")
	public ResponseEntity<?> processDoctorDetails(@RequestBody Map<String, String> doctorDetails) {
	    String email = doctorDetails.get("email");
	    String username = doctorDetails.get("username");
	    
	    DoctorsInfo info=doctorsInfoService.getDoctorByEmail(email);

	    // Process doctor details here
	    System.out.println("Received doctor details: " + info);

	    return ResponseEntity.ok(info);
	}
	
	@GetMapping("/doctor-schedule")
	public ResponseEntity<List<DoctorScheduleDTO>> getDoctorSchedule(@RequestParam String regNum) {
	    List<DoctorScheduleDTO> doctorSchedule = dateInfoService.getDoctorScheduleOne(regNum);

	    if (doctorSchedule.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // Return NO_CONTENT if list is empty
	    }

	    return ResponseEntity.status(HttpStatus.OK).body(doctorSchedule);  // Return OK with the schedule
	}

}
