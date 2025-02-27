package com.hospital.service;

import java.util.List;
import java.util.Map;

import com.hospital.dto.MeetingResponse;
import com.hospital.dto.PrescriptionDetailsDto;
import com.hospital.model.TabletInfo;

import jakarta.mail.MessagingException;

public interface TabletInfoService {

	public String saveTablets(String docRegNum,String patRegNum,String doctorNotes,List<Map<String, Object>> info) throws MessagingException;

	List<MeetingResponse> getDoctorMeetings(String date, String doctorEmail);

	List<PrescriptionDetailsDto> getDoctorPrescriptions(String docRegNum);
}
