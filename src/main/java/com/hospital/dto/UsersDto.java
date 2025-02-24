package com.hospital.dto;

public class UsersDto {
	private String email;
    private String firstName;
    private String lastName;
    private String registrationNumber;
    private String mobileNumber;

    // Constructors
    public UsersDto() {}

    public UsersDto(String email, String firstName, String lastName, String registrationNumber, String mobileNumber) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.registrationNumber = registrationNumber;
        this.mobileNumber = mobileNumber;
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
}
