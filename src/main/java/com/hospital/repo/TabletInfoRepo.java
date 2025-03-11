package com.hospital.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hospital.dto.EPrescriptionDto;
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


	
	@Query("SELECT DISTINCT new com.hospital.dto.EPrescriptionDto(" +
		       "b.firstName, b.lastName, d.date, di.name, di.specialization, di.email, b.email) " +
		       "FROM BookAppointment b, DateAndTimeInfo d, DoctorsInfo di " +
		       "WHERE d.appointmentId = b.id " +
		       "AND di.regestrationNum = d.regestrationNum " +
		       "AND b.email = :email " +
		       "AND di.regestrationNum = :registrationNum")
		EPrescriptionDto fetchEPrescriptionDetails(@Param("email") String email, @Param("registrationNum") String registrationNum);
	
	
	List<TabletInfo> findByNotesId(int notesId);



}
