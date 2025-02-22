package com.hospital.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.dto.MeetingDto;
import com.hospital.model.MeetingDetails;
import com.hospital.repo.MeetingDetailsRepo;
import com.hospital.service.MeetingService;

@Service
public class MeetingServiceImpl implements MeetingService {

	@Autowired
	private MeetingDetailsRepo repo;

	@Override
	public String getMeetingDetails(String meetId) {
		// TODO Auto-generated method stub
		MeetingDetails meet = repo.findByMeetingRoom(meetId);
		String meetingUrl = "https://meet.jit.si/" + meet.getMeetingRoom();
		return meetingUrl;
	}

	@Override
	public List<MeetingDto> getMeetingListOnDate(String date, String email) {
		// TODO Auto-generated method stub
		return repo.findMeetingsByEmailAndDate(date, email);
	}

	
	@Override
	public List<MeetingDto> getDoctorMeetingListOnDate(String date, String email) {
		// TODO Auto-generated method stub
		return repo.findDoctorMeetingsByEmailAndDate(date, email);
	}


}
