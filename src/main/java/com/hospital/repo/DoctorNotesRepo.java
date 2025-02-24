package com.hospital.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospital.model.DoctorNotesModel;

@Repository
public interface DoctorNotesRepo extends JpaRepository<DoctorNotesModel, Integer>{

}
