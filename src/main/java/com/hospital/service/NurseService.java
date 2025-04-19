package com.hospital.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hospital.dto.NurseDto;
import com.hospital.dto.NurseRequestsDto;
import com.hospital.dto.UsersDto;
import com.hospital.model.Nurse;


public interface NurseService {

	public Nurse saveNurse(NurseDto dto);

	Nurse updateNurse(int id, NurseDto dto);

	void deleteNurse(int id);


	List<UsersDto> getAllNurses(String docRegNum);


	String sendToLoginService(String userData, String email);

	List<NurseRequestsDto> getNurseRequests(String NurseRegNum);
	
}
