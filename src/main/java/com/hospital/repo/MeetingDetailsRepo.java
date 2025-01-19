package com.hospital.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospital.model.MeetingDetails;
@Repository
public interface MeetingDetailsRepo extends JpaRepository<MeetingDetails, Integer>{
	
	public MeetingDetails findByMeetingRoom(String meetingRoom);

}
