package com.hospital.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hospital.dto.MeetingDto;
import com.hospital.model.MeetingDetails;
@Repository
public interface MeetingDetailsRepo extends JpaRepository<MeetingDetails, Integer>{
	
	public MeetingDetails findByMeetingRoom(String meetingRoom);
	
	
	@Query("SELECT new com.hospital.dto.MeetingDto(di.name, d.time, m.patientUrl) " +
	           "FROM BookAppointment b " +
	           "JOIN DateAndTimeInfo d ON d.appointmentId = b.id " +
	           "JOIN DoctorsInfo di ON di.regestrationNum = d.regestrationNum " +
	           "JOIN MeetingDetails m ON m.appointmentId = b.id " +
	           "WHERE b.email = :email AND d.date = :date")
	    List<MeetingDto> findMeetingsByEmailAndDate(@Param("date") String date, @Param("email") String email);
	
	

	@Query("SELECT new com.hospital.dto.MeetingDto(" +
		       "CONCAT(b.firstName, ' ', b.lastName), d.time, m.doctorUrl) " +
		       "FROM BookAppointment b " +
		       "JOIN DateAndTimeInfo d ON d.appointmentId = b.id " +
		       "JOIN DoctorsInfo di ON di.regestrationNum = d.regestrationNum " +
		       "JOIN MeetingDetails m ON m.appointmentId = b.id " +
		       "WHERE di.email = :email AND d.date = :date")
		List<MeetingDto> findDoctorMeetingsByEmailAndDate(String date, String email);


}
