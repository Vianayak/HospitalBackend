package com.hospital.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
@Entity
public class MeetingDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String meetingRoom;
	private String password;
	private int appointmentId;
	private String patientUrl;
	private String doctorUrl;
	public String getPatientUrl() {
		return patientUrl;
	}
	public void setPatientUrl(String patientUrl) {
		this.patientUrl = patientUrl;
	}
	public String getDoctorUrl() {
		return doctorUrl;
	}
	public void setDoctorUrl(String doctorUrl) {
		this.doctorUrl = doctorUrl;
	}
	public int getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(int appointmentId) {
		this.appointmentId = appointmentId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMeetingRoom() {
		return meetingRoom;
	}
	public void setMeetingRoom(String meetingRoom) {
		this.meetingRoom = meetingRoom;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
