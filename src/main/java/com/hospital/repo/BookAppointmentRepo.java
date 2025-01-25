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
	
	// Total appointments for a specific date
	@Query("SELECT COUNT(d) FROM DateAndTimeInfo d WHERE d.regestrationNum = :doctorRegNum AND d.date = :date")
    Long getTotalAppointmentsForDate(@Param("date") String date, @Param("doctorRegNum") String doctorRegNum);

    // Accepted appointments for a specific date
	@Query("SELECT COUNT(d) " +
		       "FROM DateAndTimeInfo d " +
		       "JOIN BookAppointment b ON d.appointmentId = b.id " +
		       "WHERE d.regestrationNum = :doctorRegNum AND d.date = :date AND b.doctorStatus = 'ACCEPTED'")
		Long getAcceptedAppointmentsForDate(@Param("date") String date, @Param("doctorRegNum") String doctorRegNum);


	@Query("SELECT COUNT(d) " +
		       "FROM DateAndTimeInfo d " +
		       "JOIN BookAppointment b ON d.appointmentId = b.id " +
		       "WHERE d.regestrationNum = :doctorRegNum AND b.doctorStatus = 'ACCEPTED'")
		Long getTotalAcceptedAppointments(@Param("doctorRegNum") String doctorRegNum);


	
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
