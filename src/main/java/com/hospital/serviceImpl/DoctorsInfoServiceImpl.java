package com.hospital.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.model.DoctorsInfo;
import com.hospital.repo.DoctorInfoRepo;
import com.hospital.service.DoctorsInfoService;

@Service
public class DoctorsInfoServiceImpl implements DoctorsInfoService {

	@Autowired
	private DoctorInfoRepo doctorInfoRepo;

	 public DoctorsInfo saveDoctor(DoctorsInfo doctor) {
	        // Save the doctor to the database
	        return doctorInfoRepo.save(doctor);
	    }



}
