package com.hospital.dto;

import jakarta.persistence.Column;

public class NurseDto {

	private int id;
	private String name;
	private String email;
	private String doctorRegNum;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDoctorRegNum() {
		return doctorRegNum;
	}
	public void setDoctorRegNum(String doctorRegNum) {
		this.doctorRegNum = doctorRegNum;
	}
	
	
}
