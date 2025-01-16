package com.hospital.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.model.DateAndTimeInfo;
import com.hospital.model.DoctorsInfo;
import com.hospital.repo.DateAndTimeInfoRepo;
import com.hospital.repo.DoctorInfoRepo;
import com.hospital.service.DoctorsInfoService;

@Service
public class DoctorsInfoServiceImpl implements DoctorsInfoService {

	@Autowired
	private DoctorInfoRepo doctorInfoRepo;
	
	@Autowired
	private DateAndTimeInfoRepo dateAndTimeInfoRepo;

	 public DoctorsInfo saveDoctor(DoctorsInfo doctor) {
	        // Save the doctor to the database
	        return doctorInfoRepo.save(doctor);
	    }

	@Override
	public List<DoctorsInfo> getDoctorsList() {
		// TODO Auto-generated method stub
		return doctorInfoRepo.findAll();
	}
	
	@Override
	public DoctorsInfo getDoctorById(int doctorId) {
        return doctorInfoRepo.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found"));
    }

	




}
