package com.hospital.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.dto.NurseDto;
import com.hospital.dto.NurseRequestsDto;
import com.hospital.dto.NurseRequestsTempDto;
import com.hospital.dto.UsersDto;
import com.hospital.model.Nurse;
import com.hospital.repo.NurseRepository;
import com.hospital.service.NurseService;

import kong.unirest.HttpResponse;
import kong.unirest.HttpStatus;
import kong.unirest.Unirest;

@Service
public class NurseServiceImpl implements NurseService{
	
	
	private static final String USER_SERVICE_URL = "http://localhost:8082/api/user/user-details";
	private static final String USER_SERVICE_URL_REG_NUM = "http://localhost:8082/api/user/user-details-by-reg-num";
	
	@Autowired
	private NurseRepository repo;
	
	@Autowired
	private EmailServiceImpl mail;

	@Override
	public Nurse saveNurse(NurseDto dto) {
		// TODO Auto-generated method stub
		Nurse nurseDetails=new Nurse();
		Nurse existingNurse=repo.findByEmail(dto.getEmail());
		
		UsersDto userDetails = fetchUserDetails(dto.getEmail());
		
		if(existingNurse != null || userDetails != null) {
			throw new RuntimeException("User with this email already exists!");
			
		}else {
			nurseDetails.setDoctorRegNum(dto.getDoctorRegNum());
			nurseDetails.setEmail(dto.getEmail());
			nurseDetails.setName(dto.getName());
            Nurse savedNurse = repo.save(nurseDetails);

            // Send email with Google Form link
            mail.sendGoogleFormEmail(savedNurse.getName(), savedNurse.getEmail());

            return savedNurse;

		}
	}

	
	private UsersDto fetchUserDetails(String email) {
        HttpResponse<UsersDto> response = Unirest.get(USER_SERVICE_URL)
                .queryString("email", email)
                .header("Content-Type", "application/json")
                .asObject(UsersDto.class);

        if (response.getStatus() == HttpStatus.NOT_FOUND || response.getBody() == null) {
            return null; 
        }
        return response.getBody();
    }
	
	@Override
	public Nurse updateNurse(int id, NurseDto dto) {
	    Nurse nurse = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Nurse not found"));

	    // Update fields
	    nurse.setName(dto.getName());
	    nurse.setEmail(dto.getEmail());

	    return repo.save(nurse);
	}
	
	@Override
	public List<UsersDto> getAllNurses(String docRegNum) {
        List<Nurse> nurses=repo.findByDoctorRegNum(docRegNum);
        List<UsersDto> nurseUsers=new ArrayList<>();
        for (Nurse nurse : nurses) {
            if (nurse.getEmail() != null) {
                UsersDto userDetails = fetchUserDetails(nurse.getEmail());
                nurseUsers.add(userDetails);
            }
        }
		return nurseUsers;
    }
	
	
	@Override
	public void deleteNurse(int id) {
	    Nurse nurse = repo.findById(id)
	            .orElseThrow(() -> new NoSuchElementException("Nurse not found"));
	    
	    repo.delete(nurse);
	}

	
	@Override
	public String sendToLoginService(String userData,String email) {
		
	    try {
	    	
	    	if (!checkNurseAvailability(email)) {
	            return "Nurse not already exists in the database. Skipping user registration.";
	        }
	        // Prepare the request
	        kong.unirest.HttpResponse<String> response = Unirest.post("http://localhost:8082/api/user/register")
	                .field("user", userData)
	                .connectTimeout(10000)  // 10 seconds
	                .socketTimeout(10000)   // 10 seconds
	                .asString(); 
	        if (response.isSuccess()) {
	            // Return the response body if successful
	            return response.getBody();
	        } else {
	        	if (response.getStatus() == 500 && response.getBody().contains("Email already exists")) {
	                // Handle the specific error and return a custom message
	                return "Error: Email already exists.";
	            }
	            throw new RuntimeException("Error: " + response.getStatus() + " - " + response.getBody());
	        }
	    } catch (Exception e) {
	        throw new RuntimeException("Error sending data to the Login Service: " + e.getMessage(), e);
	    }
	}
	
	
	private boolean checkNurseAvailability(String email) {
		return repo.existsByEmail(email);
	}
	
	
	@Override
	public List<NurseRequestsDto> getNurseRequests(String NurseRegNum){
		
		List<NurseRequestsTempDto> lst=repo.findHomeServiceDetailsByNurse(NurseRegNum);
		
		
		List<NurseRequestsDto> newLst=new ArrayList<>();
		
		
		for(NurseRequestsTempDto dto:lst) {
			
			UsersDto user=fetchUserDetailsByRegNum(dto.getPatientRegNum());
			
			NurseRequestsDto req=new NurseRequestsDto();
			req.setName(user.getFirstName()+" "+user.getLastName());
			req.setDate(dto.getDate());
			req.setReason(dto.getReason());
			req.setTime(dto.getTime());
			req.setLocation(dto.getLocation());
			newLst.add(req);
		}
		
		return newLst;
		
	}

	
	private UsersDto fetchUserDetailsByRegNum(String patientRegNum) {
        HttpResponse<UsersDto> response = Unirest.get(USER_SERVICE_URL_REG_NUM)
                .queryString("regNum", patientRegNum)
                .header("Content-Type", "application/json")
                .asObject(UsersDto.class);

        if (response.getStatus() == HttpStatus.NOT_FOUND || response.getBody() == null) {
            return null; 
        }
        return response.getBody();
    }
	

}
