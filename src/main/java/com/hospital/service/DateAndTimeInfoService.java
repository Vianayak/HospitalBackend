package com.hospital.service;

import java.util.List;

import com.hospital.dto.DoctorScheduleDTO;
import com.hospital.model.DateAndTimeInfo;

public interface DateAndTimeInfoService {
	public List<DateAndTimeInfo> getDoctorSchedule(String regNum);

	List<DoctorScheduleDTO> getDoctorScheduleOne(String regNum);
}
