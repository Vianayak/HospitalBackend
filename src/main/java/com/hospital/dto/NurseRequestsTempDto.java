package com.hospital.dto;

public class NurseRequestsTempDto {

	
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getPatientRegNum() {
		return patientRegNum;
	}
	public void setPatientRegNum(String patientRegNum) {
		this.patientRegNum = patientRegNum;
	}
	private String reason;
	private String date;
	private String time;
	private String patientRegNum;
	private String location;
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public NurseRequestsTempDto(String reason, String date, String time, String patientRegNum, String location) {
		super();
		this.reason = reason;
		this.date = date;
		this.time = time;
		this.patientRegNum = patientRegNum;
		this.location = location;
	}
	
	
}
