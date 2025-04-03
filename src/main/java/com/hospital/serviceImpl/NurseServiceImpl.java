package com.hospital.serviceImpl;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.dto.NurseDto;
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
	
	@Autowired
	private NurseRepository repo;

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
			return repo.save(nurseDetails);
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
	public List<Nurse> getAllNurses(String docRegNum) {
        return repo.findByDoctorRegNum(docRegNum);
    }
	
	
	@Override
	public void deleteNurse(int id) {
	    Nurse nurse = repo.findById(id)
	            .orElseThrow(() -> new NoSuchElementException("Nurse not found"));
	    
	    repo.delete(nurse);
	}


}
