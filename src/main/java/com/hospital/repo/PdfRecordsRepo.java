package com.hospital.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hospital.dto.PatientDetailsDto;
import com.hospital.dto.PrescriptionDetailsDto;
import com.hospital.model.PdfRecord;


@Repository
public interface PdfRecordsRepo extends JpaRepository<PdfRecord, Integer>{
	
	@Query("SELECT DISTINCT new com.hospital.dto.PrescriptionDetailsDto(" +
		       "t.patientRegNum, " +
		       "p.generatedDate, " +
		       "p.generatedTime, " +
		       "p.pdfData, " +
		       "t.firstName, " +
		       "t.lastName, " +  // ✅ Added missing comma
		       "dn.doctorFeedback" +  // ✅ Now correctly formatted
		       ") " +
		       "FROM TabletInfo t, PdfRecord p, DoctorNotesModel dn " +  // ✅ Added space before WHERE
		       "WHERE p.notesId = t.notesId " +
		       "AND dn.id = t.notesId " +  // ✅ Added spacing for readability
		       "AND t.doctorRegNum = :docRegNum")
		List<PrescriptionDetailsDto> findByDocRegNum(@Param("docRegNum") String docRegNum);


	
}
