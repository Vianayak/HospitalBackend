package com.hospital.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hospital.model.DateAndTimeInfo;
import com.hospital.model.DoctorsInfo;

public interface DoctorsInfoService {
	
	public DoctorsInfo saveDoctor(DoctorsInfo doc,MultipartFile file) throws IOException;
	
	public List<DoctorsInfo> getDoctorsList();
	
	public DoctorsInfo getDoctorById(int doctorId);
	
	
	public DoctorsInfo getDoctorByEmail(String email);	

}
