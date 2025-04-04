package com.hospital.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospital.model.Nurse;

@Repository
public interface NurseRepository extends JpaRepository<Nurse, Integer>{
	
	public Nurse findByEmail(String email);

	public Optional<Nurse> findById(int id);
	
	List<Nurse> findByDoctorRegNum(String doctorRegNum);

}
