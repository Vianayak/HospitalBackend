package com.hospital.service;

import java.util.List;

import com.hospital.dto.DoctorSlotRequestDTO;
import com.hospital.model.DoctorAvailableSlots;

public interface DoctorAvailableSlotsService {

	void saveDoctorSlots(List<DoctorSlotRequestDTO> slotRequests);

	List<DoctorAvailableSlots> findByDoctorRegNum(String doctorRegNum);

}
