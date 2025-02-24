package com.hospital.dto;

public class PatientDetailsDto {

	private String email;
	private String name;
	private String regNum;
	private String time;
	public PatientDetailsDto(String email, String name, String regNum, String time) {
		super();
		this.email = email;
		this.name = name;
		this.regNum = regNum;
		this.time = time;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRegNum() {
		return regNum;
	}
	public void setRegNum(String regNum) {
		this.regNum = regNum;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
}
