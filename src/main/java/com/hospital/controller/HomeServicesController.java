package com.hospital.controller;



import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.web.multipart.MultipartFile;

import com.hospital.dto.HomeServiceRequestDto;
import com.hospital.model.HomeServicesModel;
import com.hospital.repo.HomeServicesRepo;
import com.hospital.service.HomeServicesService;

@RestController
@RequestMapping("/api/home-services")
@CrossOrigin(origins = "*")
public class HomeServicesController {
	
@Autowired
private HomeServicesService service;

@Autowired
private HomeServicesRepo repo;

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


@PutMapping("/update-status/{id}")
public ResponseEntity<String> updateStatus(@PathVariable int id, @RequestBody Map<String, String> requestData) {
    Optional<HomeServicesModel> optionalService = repo.findById(id);

    if (optionalService.isPresent()) {
        HomeServicesModel service = optionalService.get();
        service.setStatus(requestData.get("status")); // Set the status to "Accepted"
        service.setNurseRegNum(requestData.get("nurseRegNum"));
        repo.save(service);
        return ResponseEntity.ok("Status updated successfully.");
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service request not found.");
    }
}


}
