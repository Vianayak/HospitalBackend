package com.hospital.dto;

public class MeetingDto {

	private String name;
	private String time;
	private String patientUrl;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public MeetingDto(String name, String time, String patientUrl) {
		super();
		this.name = name;
		this.time = time;
		this.patientUrl = patientUrl;
	}
	public String getPatientUrl() {
		return patientUrl;
	}
	public void setPatientUrl(String patientUrl) {
		this.patientUrl = patientUrl;
	}
}
