package com.hospital.dto;

public class HomeServiceRequestDto {
    public HomeServiceRequestDto() {
	}

	public String getPatientRegNum() {
		return patientRegNum;
	}

	public void setPatientRegNum(String patientRegNum) {
		this.patientRegNum = patientRegNum;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	

	public byte[] getePrescription() {
		return ePrescription;
	}

	public void setePrescription(byte[] ePrescription) {
		this.ePrescription = ePrescription;
	}

	public byte[] getIdentityDocs() {
		return identityDocs;
	}

	public void setIdentityDocs(byte[] identityDocs) {
		this.identityDocs = identityDocs;
	}



	private String patientRegNum;
    private String patientName;
    private String requestDate;
    private String time;
    private String status;
    private byte[] ePrescription;
    private byte[] identityDocs;

    // Constructor
  

    // Getters and Setters
}

