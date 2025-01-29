package com.hospital.model;

import com.hospital.enums.DoctorSlots;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class DoctorAvailableSlots {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String slot;
	private String time;
	private String date;
	private String docRegNum;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDocRegNum() {
		return docRegNum;
	}
	public void setDocRegNum(String docRegNum) {
		this.docRegNum = docRegNum;
	}
	public String getSlot() {
		return slot;
	}
	public void setSlot(String slot) {
		this.slot = slot;
	}
}
