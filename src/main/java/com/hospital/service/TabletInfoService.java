package com.hospital.service;

import java.util.List;
import java.util.Map;

import com.hospital.dto.MeetingResponse;
import com.hospital.model.TabletInfo;

public interface TabletInfoService {

	public byte[] saveTablets(String docRegNum,String patRegNum,String doctorNotes,List<Map<String, Object>> info);

	List<MeetingResponse> getDoctorMeetings(String date, String doctorEmail);
}
