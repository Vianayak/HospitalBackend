package com.hospital.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.dto.DoctorScheduleDTO;
import com.hospital.model.DateAndTimeInfo;
import com.hospital.model.DoctorAvailableSlots;
import com.hospital.repo.DateAndTimeInfoRepo;
import com.hospital.repo.DoctorAvailableSlotsRepository;
import com.hospital.service.DateAndTimeInfoService;

@Service
public class DateAndTimeInfoServiceImpl implements DateAndTimeInfoService{
	
	@Autowired
	private DateAndTimeInfoRepo dateAndTimeInfoRepo;
	
	@Autowired
	private DoctorAvailableSlotsRepository docAvailableSlots;
	
	@Override
	public List<DateAndTimeInfo> getDoctorSchedule(String regNum) {
	    // Assuming findByRegestrationNum returns a list of doctor schedules (change the method accordingly in the repository)
	    return dateAndTimeInfoRepo.findByRegestrationNum(regNum);  // Ensure this returns a List<DateAndTimeInfo>
	}
	
	
	@Override
	public List<DoctorScheduleDTO> getDoctorScheduleOne(String regNum) {
	    // Fetch blocked slots from DateAndTimeInfo
	    List<DateAndTimeInfo> dateAndTimeInfos = dateAndTimeInfoRepo.findByRegestrationNum(regNum);

	    // Fetch available slots from DoctorAvailableSlots
	    List<DoctorAvailableSlots> availableSlots = docAvailableSlots.findByDocRegNum(regNum);

	    // Prepare the DTO response
	    Map<String, DoctorScheduleDTO> scheduleMap = new HashMap<>(); // Map to group by date

	    // Iterate through the blocked slots and organize them with available slots
	    for (DateAndTimeInfo dateInfo : dateAndTimeInfos) {
	        // Get the date
	        String date = dateInfo.getDate();

	        // Initialize the schedule if not already present
	        DoctorScheduleDTO scheduleDTO = scheduleMap.getOrDefault(date, new DoctorScheduleDTO());
	        scheduleDTO.setDate(date);

	        // Get the blocked slots for the current date and add to the list
	        List<String> blockedSlots = scheduleDTO.getBlockedSlots() != null ? scheduleDTO.getBlockedSlots() : new ArrayList<>();
	        blockedSlots.add(dateInfo.getTime());  // Add blocked slot time
	        scheduleDTO.setBlockedSlots(blockedSlots);

	        // Update the schedule map with the new or modified schedule
	        scheduleMap.put(date, scheduleDTO);
	    }

	    // Now, group the available slots by date
	    for (DoctorAvailableSlots slot : availableSlots) {
	        String date = slot.getDate();

	        // Get the schedule for the current date
	        DoctorScheduleDTO scheduleDTO = scheduleMap.get(date);
	        if (scheduleDTO != null) {
	            // Get available slots for the current date
	            List<String> availableSlotsForDate = scheduleDTO.getAvailableSlots() != null ? scheduleDTO.getAvailableSlots() : new ArrayList<>();
	            availableSlotsForDate.add(slot.getTime());  // Add available slot time
	            scheduleDTO.setAvailableSlots(availableSlotsForDate);
	        }
	    }

	    // Return the values from the map as the final list of DoctorScheduleDTOs
	    return new ArrayList<>(scheduleMap.values());
	}

}
