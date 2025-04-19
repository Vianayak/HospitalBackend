package com.hospital.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hospital.dto.NurseRequestsTempDto;
import com.hospital.model.Nurse;

@Repository
public interface NurseRepository extends JpaRepository<Nurse, Integer>{
	
	public Nurse findByEmail(String email);

	public Optional<Nurse> findById(int id);
	
	List<Nurse> findByDoctorRegNum(String doctorRegNum);
	
	boolean existsByEmail(String email);
	
	@Query("SELECT new com.hospital.dto.NurseRequestsTempDto(hs.reason,hs.date, hs.time, t.patientRegNum,hs.location) " +
		       "FROM HomeServicesModel hs " +
		       "JOIN PdfRecord pr ON pr.pdfNumb = hs.docId " +
		       "JOIN TabletInfo t ON t.notesId = pr.notesId " +
		       "WHERE hs.nurseRegNum = :nurseRegNum")
		List<NurseRequestsTempDto> findHomeServiceDetailsByNurse(@Param("nurseRegNum") String nurseRegNum);



}
