package com.hospital.serviceImpl;

import java.util.List;
import java.util.Set;
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
	    if (slotRequests.isEmpty()) {
	        return;
	    }

	    // Extract docRegNum and unique dates from incoming requests
	    String docRegNum = slotRequests.get(0).getDocRegNum();
	    List<String> requestedDates = slotRequests.stream()
	            .map(DoctorSlotRequestDTO::getDate)
	            .distinct()
	            .collect(Collectors.toList());

	    // Fetch existing slots for the given doctor and dates
	    List<DoctorAvailableSlots> existingSlots = repository.findByDocRegNumAndDateIn(docRegNum, requestedDates);

	    // Create a Set of existing slots (date + time) for quick lookup
	    Set<String> existingSlotKeys = existingSlots.stream()
	            .map(slot -> slot.getDate() + "_" + slot.getTime()) // Unique identifier for a slot
	            .collect(Collectors.toSet());

	    // Filter out slots that already exist in the database
	    List<DoctorAvailableSlots> newSlots = slotRequests.stream()
	            .filter(slotRequest -> !existingSlotKeys.contains(slotRequest.getDate() + "_" + slotRequest.getTime())) // Avoid duplicates
	            .map(slotRequest -> {
	                DoctorAvailableSlots slot = new DoctorAvailableSlots();
	                slot.setSlot(slotRequest.getSlot());
	                slot.setTime(slotRequest.getTime());
	                slot.setDate(slotRequest.getDate());
	                slot.setDocRegNum(slotRequest.getDocRegNum());
	                return slot;
	            })
	            .collect(Collectors.toList());

	    // Save only new slots
	    if (!newSlots.isEmpty()) {
	        repository.saveAll(newSlots);
	    }
	}

	
	@Override
	public List<DoctorAvailableSlots> findByDoctorRegNum(String doctorRegNum) {
	    return repository.findByDocRegNum(doctorRegNum);
	}

	
}
