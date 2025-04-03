package com.hospital.controller;



import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hospital.dto.HomeServiceRequestDto;
import com.hospital.service.HomeServicesService;

@RestController
@RequestMapping("/api/home-services")
@CrossOrigin(origins = "*")
public class HomeServicesController {
	
@Autowired
private HomeServicesService service;

@PostMapping("/save-service")
public ResponseEntity<String> saveHomeService(@RequestParam("reason") String reason,
        @RequestParam("date") String date,
        @RequestParam("time") String time,
        @RequestParam("e_prescription") MultipartFile prescription,
        @RequestParam("identity") MultipartFile identity,
        @RequestParam(value = "docId", required = false) String docId) throws IOException{
	
	service.saveHomeServices(reason,date,time,prescription,identity,docId);
	return ResponseEntity.ok("success");
}
	

@GetMapping("/home-service-requests/{doctorRegNum}")
public ResponseEntity<List<HomeServiceRequestDto>> getRequests(@PathVariable String doctorRegNum) {
    List<HomeServiceRequestDto> requests = service.getHomeServiceRequests(doctorRegNum);
    return ResponseEntity.ok(requests);
}
}
