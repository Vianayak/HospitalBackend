package com.hospital.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.dto.DoctorSlotRequestDTO;
import com.hospital.model.DoctorAvailableSlots;
import com.hospital.repo.DoctorAvailableSlotsRepository;
import com.hospital.service.DoctorAvailableSlotsService;

@Service
public class DoctorAvailableSlotsServiceImpl implements DoctorAvailableSlotsService{

	@Autowired
    private DoctorAvailableSlotsRepository repository;
	
	
	@Override
	public void saveDoctorSlots(List<DoctorSlotRequestDTO> slotRequests) {
        List<DoctorAvailableSlots> slots = slotRequests.stream().map(slotRequest -> {
            DoctorAvailableSlots slot = new DoctorAvailableSlots();
            slot.setSlot(slotRequest.getSlot());
            slot.setTime(slotRequest.getTime());
            slot.setDate(slotRequest.getDate());
            slot.setDocRegNum(slotRequest.getDocRegNum());
            return slot;
        }).collect(Collectors.toList());

        repository.saveAll(slots);
    }
	
}
