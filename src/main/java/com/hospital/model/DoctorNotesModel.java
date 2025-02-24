package com.hospital.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class DoctorNotesModel {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	
	@Lob
    @Column(columnDefinition = "MEDIUMTEXT") 
    private String notes;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
