package com.hospital.repo;

import com.hospital.dto.AppointmentDto;
import com.hospital.dto.DoctorDetailsDto;

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
    
    
    
    @Query("SELECT new com.hospital.dto.DoctorDetailsDto(di.name, d.time) " +
    	       "FROM BookAppointment b " +
    	       "JOIN DateAndTimeInfo d ON d.appointmentId = b.id " +
    	       "JOIN DoctorsInfo di ON di.regestrationNum = d.regestrationNum " +
    	       "WHERE b.email = :email " +
    	       "AND d.date = :date AND b.orderStatus='captured'")
    	public List<DoctorDetailsDto> findDoctorDetailsOnDate(@Param("email") String email, @Param("date") String date);


    @Query("SELECT SUM(b.amount) FROM BookAppointment b " +
    	       "JOIN DateAndTimeInfo d ON d.appointmentId=b.id WHERE d.date = :date " +
    	       "AND b.email = :email AND b.orderStatus = 'captured'")
    	Double calculatePaymentsForPatientDate(@Param("email") String email, @Param("date") String date);


    @Query("SELECT SUM(b.amount) FROM BookAppointment b WHERE b.email = :email AND b.orderStatus = 'captured'")
    Double calculatePaymentsForPatient(@Param("email") String email);
    
    
    
    @Query("SELECT COUNT(b) FROM BookAppointment b " +
    	       "JOIN DateAndTimeInfo d ON d.appointmentId=b.id WHERE b.email = :email " +
    	       "AND d.date = :date AND b.orderStatus = 'captured'")
    	Long getTodaysConsultations(@Param("email") String email, @Param("date") String date);
	
	
	
    @Query("SELECT COUNT(b) FROM BookAppointment b WHERE b.email = :email AND b.orderStatus = 'captured'")
    Long getTotalConsultations(@Param("email") String email);
    
    

    @Query("SELECT p.dob FROM BookAppointment p WHERE p.email = :email")
    String findDobByEmail(@Param("email") String email);
    
    @Query("SELECT p.gender FROM BookAppointment p WHERE p.email = :email")
    String findGenderByEmail(@Param("email") String email);



}
