package com.hospital.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
