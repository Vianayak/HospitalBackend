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
	private String availability;
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

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "languages")
	private String languages;
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

	public List<String> getLanguages() {
		if (languages == null || languages.isEmpty()) {
			return List.of(); // Return an empty list if null or empty
		}
		return Arrays.asList(languages.split(",")); // Split the string into a list
	}

	public void setLanguages(List<String> languages) {
		if (languages == null || languages.isEmpty()) {
			this.languages = null; // Set to null if the list is empty
		} else {
			this.languages = String.join(",", languages); // Join the list into a single string
		}
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
