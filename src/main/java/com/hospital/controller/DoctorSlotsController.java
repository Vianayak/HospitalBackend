package com.hospital.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.dto.DoctorSlotRequestDTO;
import com.hospital.model.DoctorAvailableSlots;
import com.hospital.service.DoctorAvailableSlotsService;

@RestController
@RequestMapping("/api/doctor-slots")
@CrossOrigin(origins = "*")
public class DoctorSlotsController {
	
	@Autowired
    private DoctorAvailableSlotsService service;

	
	@PostMapping("/save")
	public ResponseEntity<Map<String, String>> saveDoctorSlots(@RequestBody List<DoctorSlotRequestDTO> slotRequests) {
	    Map<String, String> response = new HashMap<>();
	    try {
	        service.saveDoctorSlots(slotRequests);
	        response.put("message", "Doctor slots saved successfully.");
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        response.put("error", "Error saving doctor slots: " + e.getMessage());
	        return ResponseEntity.status(500).body(response);
	    }
	}
	
	
	@GetMapping("/get/{doctorRegNum}")
	public ResponseEntity<List<DoctorAvailableSlots>> getDoctorSlots(@PathVariable String doctorRegNum) {
	    List<DoctorAvailableSlots> slots = service.findByDoctorRegNum(doctorRegNum);
	    return ResponseEntity.ok(slots);
	}



}
