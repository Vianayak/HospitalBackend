package com.hospital.dto;

import org.springframework.stereotype.Component;


public class AppointmentStatsDTO {
    private Long todayAppointments;
    private Long totalAppointments;
    private String doctorRegNum;

   



	public Long getTotalAppointments() {
		return totalAppointments;
	}

	public void setTotalAppointments(Long totalAppointments) {
		this.totalAppointments = totalAppointments;
	}



	
	public String getDoctorRegNum() {
		return doctorRegNum;
	}

	public void setDoctorRegNum(String doctorRegNum) {
		this.doctorRegNum = doctorRegNum;
	}

	public Long getTodayAppointments() {
		return todayAppointments;
	}

	public void setTodayAppointments(Long todayAppointments) {
		this.todayAppointments = todayAppointments;
	}
}
