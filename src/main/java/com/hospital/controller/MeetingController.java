package com.hospital.controller;

import java.util.List;
import java.util.Map;

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

import com.hospital.dto.MeetingDto;
import com.hospital.service.MeetingService;

@RestController
@RequestMapping("/api/meet")
@CrossOrigin(origins = "*")
public class MeetingController {
	@Autowired
	private MeetingService meetService;

	@PostMapping("/validateMeet")
	public ResponseEntity<String> validatePassword(@RequestBody Map<String, String> request) {
		String meetId = request.get("meetingId");
		try {
			 String link=meetService.getMeetingDetails(meetId);
			return ResponseEntity.ok(link);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Wrong meeting Id");
		}
	}
	
	@GetMapping("/getMeetingList")
	public ResponseEntity<List<MeetingDto>> getMeetingList(@RequestParam String date,@RequestParam String email){
			List<MeetingDto> lst=meetService.getMeetingListOnDate(date,email);
			if (lst.isEmpty()) {
		        System.out.println("No meetings found.");
		    }
		    return ResponseEntity.ok(lst);
	}
	
	
	@GetMapping("/getDoctorMeetingList")
	public ResponseEntity<List<MeetingDto>> getDoctorMeetingList(@RequestParam String date,@RequestParam String email){
			List<MeetingDto> lst=meetService.getDoctorMeetingListOnDate(date,email);
			if (lst.isEmpty()) {
		        System.out.println("No meetings found.");
		    }
		    return ResponseEntity.ok(lst);
	}
}
