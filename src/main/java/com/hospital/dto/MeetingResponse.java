package com.hospital.dto;

public class MeetingResponse {

	
	private UsersDto userDetails;
    private PatientDetailsDto meeting;

    public MeetingResponse(UsersDto userDetails, PatientDetailsDto meeting) {
        this.userDetails = userDetails;
        this.meeting = meeting;
    }

	public UsersDto getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UsersDto userDetails) {
		this.userDetails = userDetails;
	}

	public PatientDetailsDto getMeeting() {
		return meeting;
	}

	public void setMeeting(PatientDetailsDto meeting) {
		this.meeting = meeting;
	}	
}
