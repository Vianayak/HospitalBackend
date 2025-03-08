package com.hospital.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hospital.model.DoctorsInfo;

@Repository
public interface DoctorInfoRepo extends JpaRepository<DoctorsInfo, Integer> {
	public DoctorsInfo findByRegestrationNum(String num);
	public DoctorsInfo findByEmail(String email);
	
	@Query("SELECT d FROM DoctorsInfo d WHERE LOWER(d.specialization) IN :specializations")
	List<DoctorsInfo> findBySpecializationIn(@Param("specializations") List<String> specializations);
	
	
	@Query("SELECT DISTINCT LOWER(d.specialization) FROM DoctorsInfo d")
	List<String> getAllSpecializationNames();


}
