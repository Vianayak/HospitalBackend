package com.hospital.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospital.model.DoctorsInfo;

@Repository
public interface DoctorInfoRepo extends JpaRepository<DoctorsInfo, Integer> {
	public DoctorsInfo findByRegestrationNum(int num);
}
