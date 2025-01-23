package com.hospital.dto;

import org.springframework.stereotype.Component;


public class AppointmentStatsDTO {
    private Long totalAppointments;
    private Long acceptedAppointments;
    private Long totalTreatedPatientsByDoctor;
    private String doctorRegNum;

   

	public AppointmentStatsDTO(Long totalAppointments, Long acceptedAppointments, Long totalTreatedPatientsByDoctor,
			String doctorRegNum) {
		super();
		this.totalAppointments = totalAppointments;
		this.acceptedAppointments = acceptedAppointments;
		this.totalTreatedPatientsByDoctor = totalTreatedPatientsByDoctor;
		this.doctorRegNum = doctorRegNum;
	}

	public Long getTotalAppointments() {
		return totalAppointments;
	}

	public void setTotalAppointments(Long totalAppointments) {
		this.totalAppointments = totalAppointments;
	}

	public Long getAcceptedAppointments() {
		return acceptedAppointments;
	}

	public void setAcceptedAppointments(Long acceptedAppointments) {
		this.acceptedAppointments = acceptedAppointments;
	}

	public Long getTotalTreatedPatientsByDoctor() {
		return totalTreatedPatientsByDoctor;
	}

	public void setTotalTreatedPatientsByDoctor(Long totalTreatedPatientsByDoctor) {
		this.totalTreatedPatientsByDoctor = totalTreatedPatientsByDoctor;
	}

	public String getDoctorRegNum() {
		return doctorRegNum;
	}

	public void setDoctorRegNum(String doctorRegNum) {
		this.doctorRegNum = doctorRegNum;
	}
}
