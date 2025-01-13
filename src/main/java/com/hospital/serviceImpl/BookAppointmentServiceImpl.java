package com.hospital.serviceImpl;

import java.util.Map;
import java.util.Optional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hospital.dto.AppointmentDto;
import com.hospital.model.BookAppointment;
import com.hospital.model.DateAndTimeInfo;
import com.hospital.model.DoctorsInfo;
import com.hospital.repo.BookAppointmentRepo;
import com.hospital.repo.DateAndTimeInfoRepo;
import com.hospital.repo.DoctorInfoRepo;
import com.hospital.service.BookAppointmentService;
import com.hospital.service.EmailService;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;

@Service
public class BookAppointmentServiceImpl implements BookAppointmentService {

	@Autowired
	private BookAppointmentRepo repo;
	
	@Autowired
	private DateAndTimeInfoRepo scheduleRepo;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private DoctorInfoRepo doctorRepo;

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
		saveAppointmentDetails(dto);
		saveScheduleDetails(dto);
		DoctorsInfo doctor = doctorRepo.findByRegestrationNum(dto.getDoctorRegNum());
		emailService.sendAppointmentConfirmation(dto.getEmail(), dto.getFirstName(), dto.getLastName(), dto.getScheduledDate(), dto.getScheduledTime(), doctor.getName(), doctor.getSpecialization(), dto.getAmount());
	}

	private void saveScheduleDetails(AppointmentDto dto) {
		// TODO Auto-generated method stub
		DateAndTimeInfo info=new DateAndTimeInfo();
		info.setDate(dto.getScheduledDate());
		info.setRegestrationNum(dto.getDoctorRegNum());
		info.setSlot(dto.getSlot());
		info.setTime(dto.getScheduledTime());
		scheduleRepo.save(info);
	}

	private void saveAppointmentDetails(AppointmentDto dto) {
		// TODO Auto-generated method stub
		BookAppointment app=new BookAppointment();
		app.setFirstName(dto.getFirstName());
		app.setLastName(dto.getLastName());
		app.setAmount(dto.getAmount());
		app.setDob(dto.getDob());
		app.setDoctorRegNum(dto.getDoctorRegNum());
		app.setEmail(dto.getEmail());
		app.setGender(dto.getGender());
		app.setMobile(dto.getMobile());
		app.setOrderStatus(dto.getOrderStatus());
		app.setRazorpayOrderId(dto.getRazorpayOrderId());
		repo.save(app);
	}

	@Override
	public ResponseEntity<String> verifyPayment(Map<String, String> paymentDetails) {
		String paymentId = paymentDetails.get("razorpay_payment_id");
		String orderId = paymentDetails.get("razorpay_order_id");
		String signature = paymentDetails.get("razorpay_signature");

		// Logic to validate Razorpay signature
		boolean isValidSignature = validateRazorpaySignature(paymentId, orderId, signature);
		if (isValidSignature) {
			BookAppointment appointment = repo.findByRazorpayOrderId(orderId);
			if (appointment != null) {
				appointment.setOrderStatus("captured");
				repo.save(appointment);
				return ResponseEntity.ok("Payment verified and status updated.");
			}
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid payment verification.");
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
}
