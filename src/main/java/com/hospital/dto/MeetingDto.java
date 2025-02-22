package com.hospital.dto;

public class MeetingDto {

	private String name;
	private String time;
	private String url;
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
	public MeetingDto(String name, String time, String url) {
		super();
		this.name = name;
		this.time = time;
		this.url = url;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
