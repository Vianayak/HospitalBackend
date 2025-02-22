package com.hospital.service;

import java.util.List;

import com.hospital.dto.MeetingDto;
import com.hospital.model.MeetingDetails;

public interface MeetingService {

	public String getMeetingDetails(String meetId);

	public List<MeetingDto> getMeetingListOnDate(String date, String email);

	List<MeetingDto> getDoctorMeetingListOnDate(String date, String email);
}
