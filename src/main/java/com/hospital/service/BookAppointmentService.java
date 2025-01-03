package com.hospital.service;

import java.util.Map;

import com.hospital.model.BookAppointment;
import com.razorpay.RazorpayException;

public interface BookAppointmentService {
	public BookAppointment initiate(BookAppointment initiateAppointment) throws RazorpayException;

	public BookAppointment updateStatus(Map<String, String> map);
}
