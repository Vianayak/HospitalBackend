package com.hospital.service;

import java.util.List;

import com.hospital.model.DateAndTimeInfo;

public interface DateAndTimeInfoService {
	public List<DateAndTimeInfo> getDoctorSchedule(int regNum);
}
