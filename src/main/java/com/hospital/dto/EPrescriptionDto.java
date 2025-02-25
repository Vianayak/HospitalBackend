package com.hospital.dto;

public class EPrescriptionDto {
private String firstName;
private String lastName;
private String appointmentDate;
private String doctorName;
private String specialization;
private String doctorEmail;
private String patientEmail;

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
public String getAppointmentDate() {
	return appointmentDate;
}
public void setAppointmentDate(String appointmentDate) {
	this.appointmentDate = appointmentDate;
}
public String getDoctorName() {
	return doctorName;
}
public void setDoctorName(String doctorName) {
	this.doctorName = doctorName;
}
public String getSpecialization() {
	return specialization;
}
public void setSpecialization(String specialization) {
	this.specialization = specialization;
}
public String getPatientEmail() {
	return patientEmail;
}
public void setPatientEmail(String patientEmail) {
	this.patientEmail = patientEmail;
}
public String getDoctorEmail() {
	return doctorEmail;
}
public void setDoctorEmail(String doctorEmail) {
	this.doctorEmail = doctorEmail;
}
public EPrescriptionDto(String firstName, String lastName, String appointmentDate, String doctorName,
		String specialization, String doctorEmail,String patientEmail) {
	super();
	this.firstName = firstName;
	this.lastName = lastName;
	this.appointmentDate = appointmentDate;
	this.doctorName = doctorName;
	this.specialization = specialization;
	this.doctorEmail = doctorEmail;
	this.patientEmail = patientEmail;
	
}
}
