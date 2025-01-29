package com.hospital.service;

import java.util.List;

import com.hospital.dto.DoctorSlotRequestDTO;

public interface DoctorAvailableSlotsService {

	void saveDoctorSlots(List<DoctorSlotRequestDTO> slotRequests);

}
