package com.hospital.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospital.model.BookAppointment;
@Repository
public interface BookAppointmentRepo extends JpaRepository<BookAppointment, Integer>{

	public BookAppointment findByRazorpayOrderId(String razorpayId);

}
