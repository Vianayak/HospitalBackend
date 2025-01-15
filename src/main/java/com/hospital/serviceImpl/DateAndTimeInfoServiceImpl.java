package com.hospital.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.model.DateAndTimeInfo;
import com.hospital.repo.DateAndTimeInfoRepo;
import com.hospital.service.DateAndTimeInfoService;

@Service
public class DateAndTimeInfoServiceImpl implements DateAndTimeInfoService{
	
	@Autowired
	private DateAndTimeInfoRepo dateAndTimeInfoRepo;
	@Override
	public List<DateAndTimeInfo> getDoctorSchedule(String regNum) {
	    // Assuming findByRegestrationNum returns a list of doctor schedules (change the method accordingly in the repository)
	    return dateAndTimeInfoRepo.findByRegestrationNum(regNum);  // Ensure this returns a List<DateAndTimeInfo>
	}
}
