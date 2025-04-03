package com.hospital.controller;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.dto.NurseDto;
import com.hospital.model.Nurse;
import com.hospital.service.NurseService;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;



@RestController
@RequestMapping("/api/nurse")
@CrossOrigin(origins = "*")
public class NurseController {
	
	@Autowired
	private NurseService service;

	@PostMapping("/save")
	public ResponseEntity<?> saveNurse(@RequestBody NurseDto dto) {
		
		if (!isValidEmail(dto.getEmail())) {
            return ResponseEntity.badRequest().body("Invalid email format.");
        }
		
	    try {
	        Nurse nurse = service.saveNurse(dto);
	        return ResponseEntity.ok(nurse);
	    } catch (RuntimeException e) {  // Catching our custom exception
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body(Collections.singletonMap("error", e.getMessage())); // Sending JSON error response
	    }
	}
	
	
	
	public boolean isValidEmail(String email) {
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
            String domain = email.substring(email.indexOf("@") + 1);
            return hasMXRecords(domain);
        } catch (AddressException ex) {
            return false;
        }
    }

    private boolean hasMXRecords(String domain) {
        try {
            Properties env = new Properties();
            env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            DirContext dirContext = new InitialDirContext(env);
            Attributes attributes = dirContext.getAttributes(domain, new String[]{"MX"});
            return attributes.get("MX") != null;
        } catch (NamingException ex) {
            return false;
        }
    }

    
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateNurse(@PathVariable int id, @RequestBody NurseDto dto) {
        try {
            Nurse updatedNurse = service.updateNurse(id, dto);
            return ResponseEntity.ok(updatedNurse);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Nurse not found with ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the nurse: " + e.getMessage());
        }
    }

    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteNurse(@PathVariable int id) {
        try {
            service.deleteNurse(id);
            return ResponseEntity.ok("Nurse deleted successfully!");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Nurse not found with ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting the nurse: " + e.getMessage());
        }
    }

    
    @GetMapping("/all/{docRegNum}") // <-- Corrected Path Variable
    public ResponseEntity<?> getAllNurses(@PathVariable String docRegNum) {
        List<Nurse> nurses = service.getAllNurses(docRegNum);

        if (nurses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No nurses found.");
        }
        return ResponseEntity.ok(nurses);
    }

	
}
