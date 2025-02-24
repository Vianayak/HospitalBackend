package com.hospital.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.dto.MeetingResponse;
import com.hospital.dto.PatientDetailsDto;
import com.hospital.dto.UsersDto;
import com.hospital.model.DoctorNotesModel;
import com.hospital.model.TabletInfo;
import com.hospital.repo.DoctorNotesRepo;
import com.hospital.repo.TabletInfoRepo;
import com.hospital.service.TabletInfoService;

import jakarta.transaction.Transactional;
import kong.unirest.HttpResponse;
import kong.unirest.HttpStatus;
import kong.unirest.Unirest;

@Service
public class TabletInfoServiceImpl implements TabletInfoService{
	
	private static final String USER_SERVICE_URL = "http://localhost:8082/api/user/user-details";
	
	@Autowired
	private TabletInfoRepo repo;
	
	@Autowired
	private DoctorNotesRepo notesRepo;

	@Override
	@Transactional
	public List<TabletInfo> saveTablets(String docRegNum,String patRegNum,String doctorNotes,List<Map<String, Object>> info) {
		
		DoctorNotesModel notes=new DoctorNotesModel();
		notes.setNotes(doctorNotes);
		
		DoctorNotesModel note=notesRepo.save(notes);
		
		
		List<TabletInfo> tabletList = new ArrayList<>();

        for (Map<String, Object> tablet : info) {
            TabletInfo tabletInfo = new TabletInfo();
            tabletInfo.setPatientRegNum(patRegNum);
            tabletInfo.setDoctorRegNum(docRegNum);
            tabletInfo.setTabName((String) tablet.get("name"));
            tabletInfo.setDays(Integer.parseInt((String) tablet.get("days")));
            tabletInfo.setNotesId(note.getId());

            // Extract slots and timing
            @SuppressWarnings("unchecked")
			Map<String, Boolean> slots = (Map<String, Boolean>) tablet.get("slots");
            @SuppressWarnings("unchecked")
			Map<String, String> timing = (Map<String, String>) tablet.get("timing");

            if (slots.get("morning")) {
                tabletInfo.setSlot("morning");
                tabletInfo.setSlotTiming(timing.get("morning"));
            } else if (slots.get("afternoon")) {
                tabletInfo.setSlot("afternoon");
                tabletInfo.setSlotTiming(timing.get("afternoon"));
            } else if (slots.get("evening")) {
                tabletInfo.setSlot("evening");
                tabletInfo.setSlotTiming(timing.get("evening"));
            }

            tabletList.add(tabletInfo);
        }

		return repo.saveAll(tabletList);
	}
	
	

	@Override
	public List<MeetingResponse> getDoctorMeetings(String date, String doctorEmail) {
        // Fetch doctor meetings using JPA query
        List<PatientDetailsDto> meetingList = repo.findDoctorMeetingsByEmailAndDate(date, doctorEmail);

        // Fetch user details for each patient email
        return meetingList.stream().map(meeting -> {
        	UsersDto userDetails = fetchUserDetails(meeting.getEmail());
            return new MeetingResponse(userDetails, meeting);
        }).collect(Collectors.toList());
    }

    private UsersDto fetchUserDetails(String email) {
        HttpResponse<UsersDto> response = Unirest.get(USER_SERVICE_URL)
                .queryString("email", email)
                .header("Content-Type", "application/json")
                .asObject(UsersDto.class);

        if (response.getStatus() == HttpStatus.NOT_FOUND || response.getBody() == null) {
            return null; 
        }
        return response.getBody();
    }

    
}
