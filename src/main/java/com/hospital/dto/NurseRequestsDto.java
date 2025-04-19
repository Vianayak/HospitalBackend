package com.hospital.dto;

public class NurseRequestsDto {

	private String name;
	private String reason;
	private String date;
	private String location;
	public NurseRequestsDto(String name, String reason, String date, String location, String time) {
		super();
		this.name = name;
		this.reason = reason;
		this.date = date;
		this.location = location;
		this.time = time;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	private String time;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public NurseRequestsDto() {
		
	}
	
	
}
