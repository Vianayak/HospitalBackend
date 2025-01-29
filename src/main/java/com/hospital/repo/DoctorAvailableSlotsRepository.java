package com.hospital.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospital.model.DoctorAvailableSlots;

@Repository
public interface DoctorAvailableSlotsRepository extends JpaRepository<DoctorAvailableSlots, Integer>{

	List<DoctorAvailableSlots> findByDocRegNum(String docRegNum);  // Fetch available slots
}
