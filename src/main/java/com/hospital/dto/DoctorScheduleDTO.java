package com.hospital.dto;

import java.util.List;

public class DoctorScheduleDTO {
	private String date;  // Date in YYYY-MM-DD format
    private List<String> availableSlots;  // List of available time slots
    private List<String> blockedSlots;  // List of blocked time slots
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<String> getAvailableSlots() {
		return availableSlots;
	}
	public void setAvailableSlots(List<String> availableSlots) {
		this.availableSlots = availableSlots;
	}
	public List<String> getBlockedSlots() {
		return blockedSlots;
	}
	public void setBlockedSlots(List<String> blockedSlots) {
		this.blockedSlots = blockedSlots;
	}

}
