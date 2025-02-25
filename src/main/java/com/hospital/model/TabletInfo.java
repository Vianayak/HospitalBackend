package com.hospital.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TabletInfo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String patientRegNum;
	private String doctorRegNum;
	private String tabName;
	private int days;
	private int notesId;
	public int getNotesId() {
		return notesId;
	}
	public void setNotesId(int notesId) {
		this.notesId = notesId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPatientRegNum() {
		return patientRegNum;
	}
	public void setPatientRegNum(String patientRegNum) {
		this.patientRegNum = patientRegNum;
	}
	public String getDoctorRegNum() {
		return doctorRegNum;
	}
	public void setDoctorRegNum(String doctorRegNum) {
		this.doctorRegNum = doctorRegNum;
	}
	public String getTabName() {
		return tabName;
	}
	public void setTabName(String tabName) {
		this.tabName = tabName;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}

}
