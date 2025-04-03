package com.hospital.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hospital.dto.NurseDto;
import com.hospital.model.Nurse;


public interface NurseService {

	public Nurse saveNurse(NurseDto dto);

	Nurse updateNurse(int id, NurseDto dto);

	void deleteNurse(int id);


	List<Nurse> getAllNurses(String docRegNum);
	
}
