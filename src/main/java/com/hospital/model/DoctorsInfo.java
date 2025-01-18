package com.hospital.model;

import java.util.Arrays;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class DoctorsInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String regestrationNum;
	private String specialization;
	private String name;
	private int experience;
	 private String imagePath;
	 private String email;
	    private String mobileNumber;

	    public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getMobileNumber() {
			return mobileNumber;
		}

		public void setMobileNumber(String mobileNumber) {
			this.mobileNumber = mobileNumber;
		}

		public String getImagePath() {
			return imagePath;
		}

		public void setImagePath(String imagePath) {
			this.imagePath = imagePath;
		}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	private String location;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	

	public String getRegestrationNum() {
		return regestrationNum;
	}

	public void setRegestrationNum(String regestrationNum) {
		this.regestrationNum = regestrationNum;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
