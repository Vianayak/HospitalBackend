package com.hospital.serviceImpl;

import java.util.Base64;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hospital.model.BookAppointment;
import com.hospital.repo.BookAppointmentRepo;
import com.hospital.service.BookAppointmentService;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import jakarta.annotation.PostConstruct;

@Service
public class BookAppointmentServiceImpl implements BookAppointmentService {

	@Autowired
	private BookAppointmentRepo repo;

	private String razorpayId = "rzp_test_K5qGcFdtNC8hvm";

	private String razorpaySecret = "2Zp5B7nkfv4tS3bBDxaePh9f";

	private RazorpayClient razorpayCLient;

	@PostConstruct
	public void init() throws RazorpayException {
		this.razorpayCLient = new RazorpayClient(razorpayId, razorpaySecret);
	}

	@Override
	public BookAppointment initiate(BookAppointment initiateAppointment) throws RazorpayException {
		JSONObject options = new JSONObject();
		options.put("amount", initiateAppointment.getAmount() * 100);
		options.put("currency", "INR");
		options.put("receipt", initiateAppointment.getEmail());
		com.razorpay.Order razorpayOrder = razorpayCLient.orders.create(options);
		if (razorpayOrder != null) {
			initiateAppointment.setRazorpayOrderId(razorpayOrder.get("id"));
			initiateAppointment.setOrderStatus(razorpayOrder.get("status"));
		}
		return repo.save(initiateAppointment);
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
