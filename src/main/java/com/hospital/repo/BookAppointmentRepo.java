package com.hospital.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hospital.dto.AppointmentStatsDTO;
import com.hospital.enums.DoctorStatus;
import com.hospital.model.BookAppointment;
@Repository
public interface BookAppointmentRepo extends JpaRepository<BookAppointment, Integer>{

	public BookAppointment findByRazorpayOrderId(String razorpayId);

	public Optional<BookAppointment> findById(Integer appointmentId);
	
	@Query("SELECT new com.hospital.dto.AppointmentStatsDTO(" +
	           "COUNT(b.id), " +
	           "SUM(CASE WHEN b.doctorStatus = com.hospital.enums.DoctorStatus.ACCEPTED THEN 1 ELSE 0 END), " + // Treated patients
	           "SUM(CASE WHEN d.date <= :date THEN 1 ELSE 0 END), " + // Appointments until the given date
	           "d.regestrationNum) " + // Include doctor's registration number
	           "FROM BookAppointment b " +
	           "JOIN DateAndTimeInfo d ON b.id = d.appointmentId " +
	           "WHERE d.date <= :date AND d.regestrationNum = :doctorRegNum")
	AppointmentStatsDTO getAppointmentStatsTillDate(@Param("date") String date, @Param("doctorRegNum") String doctorRegNum);

	
	List<BookAppointment> findByIdInAndDoctorStatus(List<Integer> appointmentIds, DoctorStatus status);
	
	
	@Query("SELECT SUM(a.amount) FROM BookAppointment a " +
            "JOIN DateAndTimeInfo d ON a.id = d.appointmentId " +
            "WHERE a.orderStatus = 'captured' AND d.regestrationNum = :doctorRegNum " +
            "AND d.date = :date")
    Double calculateEarningsForDoctorOnDate(@Param("doctorRegNum") String doctorRegNum, @Param("date") String date);

    @Query("SELECT SUM(a.amount) FROM BookAppointment a " +
            "JOIN DateAndTimeInfo d ON a.id = d.appointmentId " +
            "WHERE a.orderStatus = 'captured' AND d.regestrationNum = :doctorRegNum")
    Double calculateTotalEarningsForDoctor(@Param("doctorRegNum") String doctorRegNum);



}
