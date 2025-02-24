package com.hospital.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hospital.dto.PatientDetailsDto;
import com.hospital.model.TabletInfo;


@Repository
public interface TabletInfoRepo extends JpaRepository<TabletInfo, Integer>{

	@Query("SELECT new com.hospital.dto.PatientDetailsDto(" +
		       "b.email, CONCAT(b.firstName, ' ', b.lastName), di.regestrationNum, d.time) " + 
		       "FROM BookAppointment b " +
		       "JOIN DateAndTimeInfo d ON d.appointmentId = b.id " +
		       "JOIN DoctorsInfo di ON di.regestrationNum = d.regestrationNum " +
		       "WHERE di.email = :doctorEmail AND d.date = :date")
		List<PatientDetailsDto> findDoctorMeetingsByEmailAndDate(@Param("date") String date, @Param("doctorEmail") String doctorEmail);


}
