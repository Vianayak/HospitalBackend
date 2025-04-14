package com.hospital.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class HomeServicesModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String reason;
	private String date;
	private String time;
	@Lob
	@Column(name = "e_prescription", columnDefinition = "LONGBLOB")
	private byte[] e_prescription;
	@Lob
	@Column(name = "identity", columnDefinition = "LONGBLOB")
	private byte[] identity;
	private String docId;
	
	private String status;
	 
	private String nurseRegNum;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
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
	public byte[] getE_prescription() {
		return e_prescription;
	}
	public void setE_prescription(byte[] e_prescription) {
		this.e_prescription = e_prescription;
	}
	public byte[] getIdentity() {
		return identity;
	}
	public void setIdentity(byte[] identity) {
		this.identity = identity;
	}
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	public String getNurseRegNum() {
		return nurseRegNum;
	}
	public void setNurseRegNum(String nurseRegNum) {
		this.nurseRegNum = nurseRegNum;
	}
}
