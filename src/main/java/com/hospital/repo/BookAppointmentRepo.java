package com.hospital.repo;

import com.hospital.dto.AppointmentDto;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hospital.dto.AppointmentDto;
import com.hospital.model.BookAppointment;
@Repository
public interface BookAppointmentRepo extends JpaRepository<BookAppointment, Integer>{

	public BookAppointment findByRazorpayOrderId(String razorpayId);

	public Optional<BookAppointment> findById(Integer appointmentId);
	
	
	@Query("SELECT COUNT(d) FROM DateAndTimeInfo d JOIN BookAppointment a ON a.id = d.appointmentId WHERE d.regestrationNum = :doctorRegNum AND d.date = :date AND a.orderStatus='captured'")
	Long getTodaysAppointments(@Param("doctorRegNum") String doctorRegNum, @Param("date") String date);
	
	
	
	@Query("SELECT COUNT(d) FROM DateAndTimeInfo d JOIN BookAppointment a ON a.id = d.appointmentId WHERE d.regestrationNum = :doctorRegNum AND a.orderStatus='captured'")
	Long getTotalAppointments(@Param("doctorRegNum") String doctorRegNum);
	
	
	
	@Query("SELECT SUM(a.amount) FROM BookAppointment a " +
            "JOIN DateAndTimeInfo d ON a.id = d.appointmentId " +
            "WHERE a.orderStatus = 'captured' AND d.regestrationNum = :doctorRegNum " +
            "AND d.date = :date")
    Double calculateEarningsForDoctorOnDate(@Param("doctorRegNum") String doctorRegNum, @Param("date") String date);

    @Query("SELECT SUM(a.amount) FROM BookAppointment a " +
            "JOIN DateAndTimeInfo d ON a.id = d.appointmentId " +
            "WHERE a.orderStatus = 'captured' AND d.regestrationNum = :doctorRegNum")
    Double calculateTotalEarningsForDoctor(@Param("doctorRegNum") String doctorRegNum);

    
    @Query("SELECT new com.hospital.dto.AppointmentDto(b.firstName, b.lastName, b.issue, d.time) " +
    	       "FROM BookAppointment b " +
    	       "JOIN DateAndTimeInfo d ON b.id = d.appointmentId " +
    	       "WHERE d.regestrationNum = :doctorRegNum AND d.date = :date AND b.orderStatus='captured'")
    	public List<AppointmentDto> findAppointmentsForDate(@Param("doctorRegNum") String doctorRegNum, @Param("date") String date);





}
