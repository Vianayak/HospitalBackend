package com.hospital.dto;

public class DoctorDetailsDto {

	
	private String name;
	private String time;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public DoctorDetailsDto(String name, String time) {
		super();
		this.name = name;
		this.time = time;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
}
