package com.hospital.serviceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hospital.model.DoctorsInfo;
import com.hospital.repo.DateAndTimeInfoRepo;
import com.hospital.repo.DoctorInfoRepo;
import com.hospital.service.DoctorsInfoService;

@Service
public class DoctorsInfoServiceImpl implements DoctorsInfoService {
	
	private final Path rootLocation = Paths.get("uploads");

	@Autowired
	private DoctorInfoRepo doctorInfoRepo;
	
	@Autowired
	private DateAndTimeInfoRepo dateAndTimeInfoRepo;

	 public DoctorsInfo saveDoctor(DoctorsInfo doctor,MultipartFile file) throws IOException {
	        // Save the doctor to the database
		 String imagePath = saveImage(file);
		 doctor.setImagePath(imagePath);
	      return doctorInfoRepo.save(doctor);
	    }

	@Override
	public List<DoctorsInfo> getDoctorsList() {
		// TODO Auto-generated method stub
		return doctorInfoRepo.findAll();
	}
	
	@Override
	public DoctorsInfo getDoctorById(int doctorId) {
        return doctorInfoRepo.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found"));
    }

	private String saveImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        Files.copy(file.getInputStream(), rootLocation.resolve(file.getOriginalFilename()));
        return rootLocation.resolve(file.getOriginalFilename()).toString();
    }




}
