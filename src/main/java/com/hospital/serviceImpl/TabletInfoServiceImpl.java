package com.hospital.serviceImpl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.dto.EPrescriptionDto;
import com.hospital.dto.MeetingResponse;
import com.hospital.dto.PatientDetailsDto;
import com.hospital.dto.PrescriptionDetailsDto;
import com.hospital.dto.UsersDto;
import com.hospital.model.DoctorNotesModel;
import com.hospital.model.DoctorsInfo;
import com.hospital.model.PdfRecord;
import com.hospital.model.SlotAndTimeModel;
import com.hospital.model.TabletInfo;
import com.hospital.repo.BookAppointmentRepo;
import com.hospital.repo.DoctorInfoRepo;
import com.hospital.repo.DoctorNotesRepo;
import com.hospital.repo.PdfRecordsRepo;
import com.hospital.repo.SlotAndTimeRepo;
import com.hospital.repo.TabletInfoRepo;
import com.hospital.service.TabletInfoService;

import jakarta.mail.MessagingException;
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
	
	@Autowired
	private PdfService pdfService;
	
	@Autowired
	private DoctorInfoRepo doctorInfoRepo;
	
	@Autowired
	private BookAppointmentRepo bookRepo;
	
	@Autowired
	private EmailServiceImpl emailService;
	
	@Autowired
	private SlotAndTimeRepo slotAndTimeRepo;
	
	@Autowired
	private PdfRecordsRepo pdfRecordRepository;

	@Override
	@Transactional
	public String saveTablets(String docRegNum, String patientEmail, String doctorNotes, List<Map<String, Object>> info) throws MessagingException {

	    // Save doctor's notes
	    DoctorNotesModel notes = new DoctorNotesModel();
	    notes.setNotes(doctorNotes);
	    DoctorNotesModel savedNote = notesRepo.save(notes);

	    // Fetch patient details
	    UsersDto patientDetails = fetchUserDetails(patientEmail);

	    List<TabletInfo> tabletList = new ArrayList<>();
	    List<SlotAndTimeModel> slotAndTimeList = new ArrayList<>();

	    // Save tablet information
	    for (Map<String, Object> tablet : info) {
	        TabletInfo tabletInfo = new TabletInfo();
	        tabletInfo.setPatientRegNum(patientDetails.getRegistrationNumber());
	        tabletInfo.setDoctorRegNum(docRegNum);
	        tabletInfo.setTabName((String) tablet.get("name"));
	        tabletInfo.setDays(Integer.parseInt((String) tablet.get("days")));
	        tabletInfo.setNotesId(savedNote.getId());
	        tabletInfo.setFirstName(patientDetails.getFirstName());
	        tabletInfo.setLastName(patientDetails.getLastName());

	        TabletInfo savedTablet = repo.save(tabletInfo); // Save tablet info and retrieve saved entity

	        // Extract slots and timing
	        @SuppressWarnings("unchecked")
	        Map<String, Boolean> slots = (Map<String, Boolean>) tablet.get("slots");
	        @SuppressWarnings("unchecked")
	        Map<String, String> timing = (Map<String, String>) tablet.get("timing");

	        // Save slot and timing for each tablet
	        for (String slot : slots.keySet()) {
	            if (Boolean.TRUE.equals(slots.get(slot))) {
	                SlotAndTimeModel slotAndTime = new SlotAndTimeModel();
	                slotAndTime.setTabletId(savedTablet.getId());
	                slotAndTime.setSlot(slot);
	                slotAndTime.setTiming(timing.get(slot));
	                slotAndTimeList.add(slotAndTime);
	            }
	        }
	    }

	    slotAndTimeRepo.saveAll(slotAndTimeList); // Save all slots and timings

	    // Prepare age and gender details
	    String dob = bookRepo.findDobByEmail(patientEmail);
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    LocalDate dobDate = LocalDate.parse(dob, formatter);
	    int age = Period.between(dobDate, LocalDate.now()).getYears();
	    String gender = bookRepo.findGenderByEmail(patientEmail);
	    char sex = gender.equalsIgnoreCase("Male") ? 'M' : gender.equalsIgnoreCase("Female") ? 'F' : 'O';

	    // Generate PDF
	    DoctorsInfo docInfo = doctorInfoRepo.findByRegestrationNum(docRegNum);
	    List<TabletInfo> savedTablets = repo.findByNotesId(savedNote.getId());
	    byte[] pdf = pdfService.generatePdf(docInfo, patientDetails, age, sex, doctorNotes, savedTablets);
	    
	    
	    PdfRecord pdfRecord = new PdfRecord();
	    pdfRecord.setNotesId(savedNote.getId());
	    pdfRecord.setPdfData(pdf);
	    pdfRecord.setGeneratedDate(LocalDate.now());
	    pdfRecord.setGeneratedTime(LocalTime.now());
	    pdfRecordRepository.save(pdfRecord);

	    // Send Email
	    EPrescriptionDto dto = repo.fetchEPrescriptionDetails(patientEmail, docRegNum);
	    emailService.sendEPrescriptionEmailToPtient(dto, pdf);

	    return "Saved Successfully";
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
    
    
    @Override
    public List<PrescriptionDetailsDto> getDoctorPrescriptions(String docRegNum){
    	
    	List<PrescriptionDetailsDto> lst=pdfRecordRepository.findByDocRegNum(docRegNum);
		return lst;
    	
    }

    
}
