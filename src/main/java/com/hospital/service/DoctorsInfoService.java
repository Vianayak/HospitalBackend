package com.hospital.service;

import java.util.List;

import com.hospital.model.DoctorsInfo;

public interface DoctorsInfoService {
	
	public DoctorsInfo saveDoctor(DoctorsInfo doc);
	
	public List<DoctorsInfo> getDoctorsList();

}
