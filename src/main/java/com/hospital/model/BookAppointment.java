package com.hospital.model;

import com.hospital.enums.DoctorStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class BookAppointment {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String firstName;
	private String lastName;
	private Long mobile;
	private String email;
	private String dob;
	private String gender;
	private String orderStatus;
	private String razorpayOrderId;
	private Integer amount;
	@Enumerated(EnumType.STRING) // Use EnumType.ORDINAL if you prefer numbers
    @Column(name = "doctor_status", nullable = false)
    private DoctorStatus doctorStatus;
	private Long issueId;
	
	public String getOrderStatus() {
		return orderStatus;
	}
	public DoctorStatus getDoctorStatus() {
		return doctorStatus;
	}
	public void setDoctorStatus(DoctorStatus doctorStatus) {
		this.doctorStatus = doctorStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getRazorpayOrderId() {
		return razorpayOrderId;
	}
	public void setRazorpayOrderId(String razorpayOrderId) {
		this.razorpayOrderId = razorpayOrderId;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public Long getMobile() {
		return mobile;
	}
	public void setMobile(Long mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Long getIssueId() {
		return issueId;
	}
	public void setIssueId(Long issueId) {
		this.issueId = issueId;
	}
}
