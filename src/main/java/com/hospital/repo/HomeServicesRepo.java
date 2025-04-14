package com.hospital.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hospital.model.HomeServicesModel;

@Repository
public interface HomeServicesRepo extends JpaRepository<HomeServicesModel, Integer> {

	@Query(value = "SELECT h.date, h.time, h.e_prescription, h.identity, t.patient_reg_num, h.id,h.status " +
            "FROM home_services_model h " +   // ✅ Change to exact table name
            "JOIN pdf_records p ON p.pdf_numb = h.doc_id " +
            "JOIN tablet_info t ON t.notes_id = p.notes_id " +
            "WHERE t.doctor_reg_num = :doctorRegNum", nativeQuery = true)
List<Object[]> findHomeServiceRequests(String doctorRegNum);


}
