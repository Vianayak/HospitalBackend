package com.hospital.serviceImpl;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
        if(razorpayOrder != null) {
        	initiateAppointment.setRazorpayOrderId(razorpayOrder.get("id"));
        	initiateAppointment.setOrderStatus(razorpayOrder.get("status"));
            }
		return repo.save(initiateAppointment);
	}

	@Override
	public BookAppointment updateStatus(Map<String, String> map) {
		String razorpayId = map.get("razorpay_order_id");
		BookAppointment bookAppointment = repo.findByRazorpayOrderId(razorpayId);
		bookAppointment.setOrderStatus("PAYMENT DONE");
		BookAppointment bookAppointments = repo.save(bookAppointment);
    	return bookAppointments;
	}

}
