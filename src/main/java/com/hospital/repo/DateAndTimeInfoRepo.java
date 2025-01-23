package com.hospital.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospital.model.DateAndTimeInfo;

@Repository
public interface DateAndTimeInfoRepo extends JpaRepository<DateAndTimeInfo, Integer> {
	List<DateAndTimeInfo> findByRegestrationNum(String regNum);
	DateAndTimeInfo findByAppointmentId(int appId);
	
	List<DateAndTimeInfo> findByDateAndRegestrationNum(String date, String regestrationNum);
}
