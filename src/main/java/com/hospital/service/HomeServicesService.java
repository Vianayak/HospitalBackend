package com.hospital.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hospital.dto.HomeServiceRequestDto;
import com.hospital.model.HomeServicesModel;

public interface HomeServicesService {



	public List<HomeServiceRequestDto> getHomeServiceRequests(String doctorRegNum);

	void saveHomeServices(String reason, String date, String time, MultipartFile prescription, MultipartFile identity,
			String docId, String location) throws IOException;
}
