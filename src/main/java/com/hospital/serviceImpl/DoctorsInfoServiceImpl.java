package com.hospital.serviceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

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
	    
	    // Generate a unique file name using UUID
	    String originalFileName = file.getOriginalFilename();
	    String fileExtension = getFileExtension(originalFileName);
	    String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

	    // Resolve the target path with the unique file name
	    Path targetPath = rootLocation.resolve(uniqueFileName);

	    // Copy the file to the target location
	    Files.copy(file.getInputStream(), targetPath);

	    return targetPath.toString();
	}

	private String getFileExtension(String fileName) {
	    // Get the file extension from the original file name
	    int dotIndex = fileName.lastIndexOf(".");
	    return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
	}





}
