package com.hospital.serviceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hospital.dto.HomeServiceRequestDto;
import com.hospital.dto.UsersDto;
import com.hospital.model.HomeServicesModel;
import com.hospital.repo.HomeServicesRepo;
import com.hospital.service.HomeServicesService;

import kong.unirest.HttpResponse;
import kong.unirest.HttpStatus;
import kong.unirest.Unirest;

@Service
public class HomeServicesServiceImpl implements HomeServicesService {
	
	private static final String USER_SERVICE_URL = "http://localhost:8082/api/user/user-details-by-reg-num";
    
    @Autowired
    private HomeServicesRepo repo;
    
    private String convertBlobToString(byte[] blobData) {
        return blobData != null ? new String(blobData) : null;
    }


	@Override
	public void saveHomeServices(String reason, String date, String time, MultipartFile prescription,
			MultipartFile identity, String docId) throws IOException {
		byte[] prescriptionBytes = prescription.getBytes();
        byte[] identityBytes = identity.getBytes();
        
        HomeServicesModel model=new HomeServicesModel();
        model.setDate(date);
        model.setDocId(docId);
        model.setE_prescription(prescriptionBytes);
        model.setIdentity(identityBytes);
        model.setReason(reason);
        model.setTime(time);
        repo.save(model);
	}


	@Override
	public List<HomeServiceRequestDto> getHomeServiceRequests(String doctorRegNum) {
        List<Object[]> results = repo.findHomeServiceRequests(doctorRegNum);
        List<HomeServiceRequestDto> homeServiceRequestDtos = new ArrayList<>();

        for (Object[] row : results) {
        	 HomeServiceRequestDto dto = new HomeServiceRequestDto();
             dto.setRequestDate((String) row[0]);
             dto.setTime((String) row[1]);
             dto.setePrescription((byte[])row[2]); // Convert BLOB to String
             dto.setIdentityDocs((byte[])row[3]); // Convert BLOB to String
             dto.setPatientRegNum((String) row[4]);
             dto.setId((Integer) row[5]);
             dto.setStatus((String) row[6]);

            // Fetch patient details from another microservice
            UsersDto userDto = fetchUserDetails(dto.getPatientRegNum());
            
            String patientname=userDto.getFirstName()+" "+userDto.getLastName();

            String patientName = (userDto != null) ? patientname: "Unknown";

            dto.setPatientName(patientName);
            // Map to DTO

            homeServiceRequestDtos.add(dto);
        }

        return homeServiceRequestDtos;
    }

	
	private UsersDto fetchUserDetails(String patientRegNum) {
        HttpResponse<UsersDto> response = Unirest.get(USER_SERVICE_URL)
                .queryString("regNum", patientRegNum)
                .header("Content-Type", "application/json")
                .asObject(UsersDto.class);

        if (response.getStatus() == HttpStatus.NOT_FOUND || response.getBody() == null) {
            return null; 
        }
        return response.getBody();
    }


}
