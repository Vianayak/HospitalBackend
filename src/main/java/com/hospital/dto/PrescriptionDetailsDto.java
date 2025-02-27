package com.hospital.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class PrescriptionDetailsDto {
	private String patientRegNum;
    private LocalDate generatedDate;
    private LocalTime generatedTime;
    private byte[] pdfData;
    private String firstName;
    private String lastName;

    // Constructor matching the selected fields
    public PrescriptionDetailsDto(String patientRegNum, LocalDate generatedDate, LocalTime generatedTime, byte[] pdfData, String firstName, String lastName) {
        this.patientRegNum = patientRegNum;
        this.generatedDate = generatedDate;
        this.generatedTime = generatedTime;
        this.pdfData = pdfData;
        this.firstName = firstName;
        this.lastName = lastName;
    }

	public String getPatientRegNum() {
		return patientRegNum;
	}

	public void setPatientRegNum(String patientRegNum) {
		this.patientRegNum = patientRegNum;
	}

	public LocalDate getGeneratedDate() {
		return generatedDate;
	}

	public void setGeneratedDate(LocalDate generatedDate) {
		this.generatedDate = generatedDate;
	}

	public LocalTime getGeneratedTime() {
		return generatedTime;
	}

	public void setGeneratedTime(LocalTime generatedTime) {
		this.generatedTime = generatedTime;
	}

	public byte[] getPdfData() {
		return pdfData;
	}

	public void setPdfData(byte[] pdfData) {
		this.pdfData = pdfData;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
