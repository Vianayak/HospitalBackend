package com.hospital.serviceImpl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.dto.AppointmentDto;
import com.hospital.dto.UserDto;
import com.hospital.model.BookAppointment;
import com.hospital.model.DateAndTimeInfo;
import com.hospital.model.DoctorsInfo;
import com.hospital.model.MeetingDetails;
import com.hospital.repo.BookAppointmentRepo;
import com.hospital.repo.DateAndTimeInfoRepo;
import com.hospital.repo.DoctorInfoRepo;
import com.hospital.repo.IssueRepository;
import com.hospital.repo.MeetingDetailsRepo;
import com.hospital.service.BookAppointmentService;
import com.hospital.service.EmailService;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import kong.unirest.HttpRequest;
import kong.unirest.HttpRequestWithBody;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

@Service
public class BookAppointmentServiceImpl implements BookAppointmentService {

    @Autowired
    private IssueRepository issueRepository;

	@Autowired
	private BookAppointmentRepo repo;
	
	@Autowired
	private DateAndTimeInfoRepo scheduleRepo;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private DoctorInfoRepo doctorRepo;
	
	@Autowired
	private MeetingDetailsRepo meetRepo;
	
	@Autowired
	private DateAndTimeInfoRepo dateAndTimeInfoRepo;

	private String razorpayId = "rzp_test_K5qGcFdtNC8hvm";

	private String razorpaySecret = "2Zp5B7nkfv4tS3bBDxaePh9f";

	private RazorpayClient razorpayCLient;

	@PostConstruct
	public void init() throws RazorpayException {
		this.razorpayCLient = new RazorpayClient(razorpayId, razorpaySecret);
	}

	@Override
	public void initiate(AppointmentDto dto) throws RazorpayException, MessagingException {
		JSONObject options = new JSONObject();
		options.put("amount", dto.getAmount() * 100);
		options.put("currency", "INR");
		options.put("receipt", dto.getEmail());
		com.razorpay.Order razorpayOrder = razorpayCLient.orders.create(options);
		if (razorpayOrder != null) {
			dto.setRazorpayOrderId(razorpayOrder.get("id"));
			dto.setOrderStatus(razorpayOrder.get("status"));
		}
		BookAppointment app=saveAppointmentDetails(dto);
		saveScheduleDetails(dto,app.getId());
		UserDto userDto=new UserDto();
		userDto.setEmail(app.getEmail());
		userDto.setFirstName(app.getFirstName());
		userDto.setLastName(app.getLastName());
		userDto.setMobileNumber(Long.toString(app.getMobile()));
		userDto.setRole("patient");
		try {
	        ObjectMapper objectMapper = new ObjectMapper();
	        String userData = objectMapper.writeValueAsString(userDto);
	        
	        // Send the JSON string to the login service
	        String message = sendToLoginService(userData, null);
	    } catch (Exception e) {
	        throw new RuntimeException("Error converting UserDto to JSON: " + e.getMessage(), e);
	    }
		
	}
	
	
	public String sendToLoginService(String userData, MultipartFile image) {
	    try {
	        // Prepare the request
	        kong.unirest.HttpResponse<String> response = Unirest.post("http://localhost:8082/api/user/register")
	                .field("user", userData)
	                .asString(); // Send the request and get the response as a string

	        // Return the response body
	        if (response.isSuccess()) {
	            return response.getBody();
	        } else {
	            throw new RuntimeException("Error: " + response.getStatus() + " - " + response.getBody());
	        }
	    } catch (Exception e) {
	        throw new RuntimeException("Error sending data to the Login Service: " + e.getMessage(), e);
	    }
	}



	private void saveScheduleDetails(AppointmentDto dto,int id) {
		// TODO Auto-generated method stub
		DateAndTimeInfo info=new DateAndTimeInfo();
		info.setDate(dto.getScheduledDate());
		info.setRegestrationNum(dto.getDoctorRegNum());
		info.setSlot(dto.getSlot());
		info.setTime(dto.getScheduledTime());
		info.setAppointmentId(id);
		scheduleRepo.save(info);
	}

	private BookAppointment saveAppointmentDetails(AppointmentDto dto) {
		// TODO Auto-generated method stub
		BookAppointment app=new BookAppointment();
		app.setFirstName(dto.getFirstName());
		app.setLastName(dto.getLastName());
		app.setAmount(dto.getAmount());
		app.setDob(dto.getDob());
		app.setEmail(dto.getEmail());
		app.setGender(dto.getGender());
		app.setMobile(dto.getMobile());
		app.setOrderStatus(dto.getOrderStatus());
		app.setRazorpayOrderId(dto.getRazorpayOrderId());
		/* app.setDoctorStatus(DoctorStatus.NOTGIVEN); */
		app.setIssue(dto.getIssue());
		return repo.save(app);
	}

	@Override
	@Transactional
	public ResponseEntity<String> verifyPayment(Map<String, String> paymentDetails) throws MessagingException {
		String paymentId = paymentDetails.get("razorpay_payment_id");
		String orderId = paymentDetails.get("razorpay_order_id");
		String signature = paymentDetails.get("razorpay_signature");

		// Logic to validate Razorpay signature
		boolean isValidSignature = validateRazorpaySignature(paymentId, orderId, signature);
		if (isValidSignature) {
			BookAppointment appointment = repo.findByRazorpayOrderId(orderId);
			if (appointment != null) {
				appointment.setOrderStatus("captured");
				BookAppointment app=repo.save(appointment);
				DateAndTimeInfo info=scheduleRepo.findByAppointmentId(app.getId());
				DoctorsInfo doctor = doctorRepo.findByRegestrationNum(info.getRegestrationNum());
				MeetingDetails meet=saveMeetingDetails(app,doctor);
				sendMeetingDetails(meet,app,doctor,info);
				emailService.sendAppointmentConfirmation(app.getEmail(), app.getFirstName(), app.getLastName(), info.getDate(), info.getTime(), doctor.getName(), doctor.getSpecialization(), app.getAmount());
				return ResponseEntity.ok("Payment verified and status updated.");
			}
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid payment verification.");
	}

	private void sendMeetingDetails(MeetingDetails meet, BookAppointment app, DoctorsInfo doctor, DateAndTimeInfo info) throws MessagingException {
		// TODO Auto-generated method stub
		String meetingURL = "https://meet.jit.si/" + meet.getMeetingRoom();
		String doctorURL = meetingURL + "?config.startWithAudioMuted=true"; // Doctor can host
	    String patientURL = meetingURL + "?config.startWithAudioMuted=true&config.prejoinPageEnabled=false"; // Patient can only join
	    emailService.sendMeetingToDoctor(meet, app, doctor, info, doctorURL);
	    emailService.sendMeetingToPatient(meet, app, doctor, info, patientURL);
	}

	private MeetingDetails saveMeetingDetails(BookAppointment app, DoctorsInfo doctor) {
		// TODO Auto-generated method stub
		MeetingDetails meet=new MeetingDetails();
		meet.setAppointmentId(app.getId());
		meet.setMeetingRoom(generateMeetingRoom(app,doctor));
		meet.setPassword(hashPassword(generatePassword(app.getFirstName(),doctor.getRegestrationNum())));
		return meetRepo.save(meet);
	}
	
	public String generatePassword(String patientFirstName, String doctorRegNum) {
        if (patientFirstName == null || patientFirstName.length() < 4 || doctorRegNum == null || doctorRegNum.length() < 4) {
            throw new IllegalArgumentException("Invalid patient first name or doctor registration number.");
        }

        // Get the first four letters of the patient's first name
        String patientNamePart = patientFirstName.substring(0, 4);
        // Capitalize the first letter
        patientNamePart = patientNamePart.substring(0, 1).toUpperCase() + patientNamePart.substring(1);

        // Get the last four digits of the doctor's registration number
        String doctorRegNumPart = doctorRegNum.substring(doctorRegNum.length() - 4);

        // Combine to form the password
        String password = patientNamePart + "@" + doctorRegNumPart;
        return password;
    }
	
	public String hashPassword(String password) {
        try {
            // Create a MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Hash the password bytes
            byte[] hashedBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            // Encode the hashed bytes to a Base64 string
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error while hashing password", e);
        }
    }

	private String generateMeetingRoom(BookAppointment app, DoctorsInfo doctor) {
		// TODO Auto-generated method stub
		Random random = new Random();
		int randomNumber = 10000 + random.nextInt(90000);
		String meetingRoom = "MEET_" + randomNumber;
		return meetingRoom;
	}

	public static boolean validateRazorpaySignature(String paymentId, String orderId, String signature) {
        try {
            String secret = "2Zp5B7nkfv4tS3bBDxaePh9f";
            String payload = orderId + "|" + paymentId;

            // Logging the payload to check for any issues
            System.out.println("Payload: " + payload);

            // Create HMAC SHA256 signature
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256");
            sha256Hmac.init(secretKey);

            // Generate hash
            byte[] hash = sha256Hmac.doFinal(payload.getBytes("UTF-8"));

            // Convert the hash to Hexadecimal format
            String generatedSignature = bytesToHex(hash);
            System.out.println("Generated Signature (Hex): " + generatedSignature);
            System.out.println("Provided Signature: " + signature);

            // Compare the generated signature with the provided signature
            return generatedSignature.equals(signature);
        } catch (Exception e) {
            System.err.println("Signature validation failed: " + e.getMessage());
            return false;
        }
    }

    // Utility function to convert byte array to Hexadecimal string
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
    
	/*
	 * @Override public void updateAppointmentStatus(int appointmentId, DoctorStatus
	 * newStatus) { // Fetch the appointment by its ID BookAppointment appointment =
	 * repo.findById(appointmentId) .orElseThrow(() -> new
	 * RuntimeException("Appointment not found"));
	 * 
	 * // Update the status appointment.setDoctorStatus(newStatus);
	 * 
	 * // Save the updated appointment repo.save(appointment); }
	 */
    
//    @Override
//    public AppointmentStatsDTO getStatsForDate(String date,String doctorRegNum) {
//    	AppointmentStatsDTO stats = new AppointmentStatsDTO();
//
//        // Total appointments for the specific date
//        stats.setTotalAppointments(repo.getTotalAppointmentsForDate(date, doctorRegNum));
//
//        // Accepted appointments for the specific date
//        stats.setAcceptedAppointments(repo.getAcceptedAppointmentsForDate(date, doctorRegNum));
//
//        // Total treated patients by the doctor till the given date
//        stats.setTotalTreatedPatientsByDoctor(repo.getTotalAcceptedAppointments(doctorRegNum));
//
//        stats.setDoctorRegNum(doctorRegNum);
//
//        return stats;
//    }
    
	/*
	 * @Override public List<Map<String, Object>> getAppointmentsWithIssues(String
	 * date, String doctorRegNum) {
	 * 
	 * // Step 1: Fetch DateAndTimeInfo records for the given date and doctorRegNum
	 * List<DateAndTimeInfo> dateAndTimeInfos =
	 * dateAndTimeInfoRepo.findByDateAndRegestrationNum(date, doctorRegNum);
	 * 
	 * // Step 2: Extract appointmentIds from DateAndTimeInfo List<Integer>
	 * appointmentIds = dateAndTimeInfos.stream()
	 * .map(DateAndTimeInfo::getAppointmentId) .collect(Collectors.toList());
	 * 
	 * // Step 3: Fetch BookAppointment records with status 'NOTGIVEN'
	 * List<BookAppointment> appointments =
	 * repo.findByIdInAndDoctorStatus(appointmentIds, DoctorStatus.NOTGIVEN);
	 * 
	 * // Step 4: Collect all issue IDs from the appointments List<Long> issueIds =
	 * appointments.stream() .flatMap(appointment ->
	 * appointment.getIssueIds().stream()) .distinct()
	 * .collect(Collectors.toList());
	 * 
	 * // Step 5: Fetch issue details List<Issue> issues =
	 * issueRepository.findByIdIn(issueIds);
	 * 
	 * // Step 6: Create a map of issueId -> issueName Map<Long, String>
	 * issueIdToNameMap = issues.stream() .collect(Collectors.toMap( Issue::getId,
	 * Issue::getIssueName, (existing, replacement) -> existing // Keep the existing
	 * value if duplicates are found ));
	 * 
	 * 
	 * // Step 7: Map the appointments with their corresponding issue names
	 * List<Map<String, Object>> result = new ArrayList<>(); for (BookAppointment
	 * appointment : appointments) { Map<String, Object> appointmentDetails = new
	 * HashMap<>(); appointmentDetails.put("appointmentId", appointment.getId());
	 * appointmentDetails.put("firstName", appointment.getFirstName());
	 * appointmentDetails.put("lastName", appointment.getLastName());
	 * appointmentDetails.put("mobile", appointment.getMobile());
	 * appointmentDetails.put("email", appointment.getEmail());
	 * appointmentDetails.put("doctorStatus", appointment.getDoctorStatus());
	 * 
	 * // Map issueIds to their names List<String> issueNames =
	 * appointment.getIssueIds().stream() .map(issueIdToNameMap::get)
	 * .collect(Collectors.toList()); appointmentDetails.put("issues", issueNames);
	 * 
	 * result.add(appointmentDetails); }
	 * 
	 * return result; }
	 * 
	 * @Override public List<Map<String, Object>>
	 * getAppointmentsWithIssuesForAccepted(String date, String doctorRegNum) {
	 * 
	 * // Step 1: Fetch DateAndTimeInfo records for the given date and doctorRegNum
	 * List<DateAndTimeInfo> dateAndTimeInfos =
	 * dateAndTimeInfoRepo.findByDateAndRegestrationNum(date, doctorRegNum);
	 * 
	 * // Step 2: Extract appointmentIds from DateAndTimeInfo List<Integer>
	 * appointmentIds = dateAndTimeInfos.stream()
	 * .map(DateAndTimeInfo::getAppointmentId) .collect(Collectors.toList());
	 * 
	 * // Step 3: Fetch BookAppointment records with status 'ACCEPTED'
	 * List<BookAppointment> appointments =
	 * repo.findByIdInAndDoctorStatus(appointmentIds, DoctorStatus.ACCEPTED);
	 * 
	 * // Step 4: Collect all issue IDs from the appointments List<Long> issueIds =
	 * appointments.stream() .flatMap(appointment ->
	 * appointment.getIssueIds().stream()) .distinct()
	 * .collect(Collectors.toList());
	 * 
	 * // Step 5: Fetch issue details List<Issue> issues =
	 * issueRepository.findByIdIn(issueIds);
	 * 
	 * // Step 6: Create a map of issueId -> issueName Map<Long, String>
	 * issueIdToNameMap = issues.stream() .collect(Collectors.toMap( Issue::getId,
	 * Issue::getIssueName, (existing, replacement) -> existing // Keep the existing
	 * value if duplicates are found ));
	 * 
	 * // Step 7: Create a map of appointmentId -> time from DateAndTimeInfo
	 * Map<Integer, String> appointmentIdToTimeMap = dateAndTimeInfos.stream()
	 * .collect(Collectors.toMap( DateAndTimeInfo::getAppointmentId,
	 * DateAndTimeInfo::getTime ));
	 * 
	 * // Step 8: Map the appointments with their corresponding issue names and time
	 * List<Map<String, Object>> result = new ArrayList<>(); for (BookAppointment
	 * appointment : appointments) { Map<String, Object> appointmentDetails = new
	 * HashMap<>(); appointmentDetails.put("appointmentId", appointment.getId());
	 * appointmentDetails.put("firstName", appointment.getFirstName());
	 * appointmentDetails.put("lastName", appointment.getLastName());
	 * appointmentDetails.put("mobile", appointment.getMobile());
	 * appointmentDetails.put("email", appointment.getEmail());
	 * appointmentDetails.put("doctorStatus", appointment.getDoctorStatus());
	 * 
	 * // Get the time for the appointment from the DateAndTimeInfo map String
	 * appointmentTime = appointmentIdToTimeMap.get(appointment.getId());
	 * appointmentDetails.put("time", appointmentTime);
	 * 
	 * // Map issueIds to their names List<String> issueNames =
	 * appointment.getIssueIds().stream() .map(issueIdToNameMap::get)
	 * .collect(Collectors.toList()); appointmentDetails.put("issues", issueNames);
	 * 
	 * result.add(appointmentDetails); }
	 * 
	 * return result; }
	 */
    
    @Override
    public Double getTodayEarnings(String doctorRegNum, String date) {
        return repo.calculateEarningsForDoctorOnDate(doctorRegNum, date);
    }

    @Override
    public Double getTotalEarnings(String doctorRegNum) {
        return repo.calculateTotalEarningsForDoctor(doctorRegNum);
    }


}
