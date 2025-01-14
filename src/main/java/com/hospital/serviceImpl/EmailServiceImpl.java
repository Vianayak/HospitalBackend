package com.hospital.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.hospital.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void sendOTPEmail(String email, String otp) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("Your OTP Code");
		message.setText("Dear User,\n\nYour OTP is: " + otp + "\n\nThis OTP is valid for 5 minutes.");
		mailSender.send(message);
	}

	public String getEmailTemplate(String firstName, String lastName, String date, String time, String doctorName,
			String specialty, int amount) {
		return 
				"Hello " + firstName + " " + lastName + ",<br/><br/>"
				+ "Thank you for booking an appointment with us! Here are the details of your upcoming visit:<br/><br/>"
				+ "<b>Appointment Details</b><br/>" + "Date: " + date + "<br/>" + "Time: " + time + "<br/>" + "Doctor: "
				+ doctorName + "<br/>" + "Specialty: " + specialty + "<br/>" + "Clinic Name: Jaya Hospitals<br/>"
				+ "Location: 123 Health Avenue, Suite 456, New York, NY 10001<br/><br/>" + "<b>Payment Details</b><br/>"
				+ "Payment Status: Paid<br/>" + "Amount Paid: " + amount + "<br/>"
				+ "Payment Method: Credit Card<br/><br/>"
				+ "Please arrive 10 minutes early for your appointment. Bring any previous medical records if applicable.<br/><br/>"
				+ "<b>Contact Information</b><br/>"
				+ "For any queries, contact us at (123) 456-7890 or support@heartcareclinic.com.<br/><br/>"
				+ "Looking forward to serving you,<br/>" + "Jaya Hospitals Team<br/><br/>" + "Jaya Hospitals<br/>"
				+ "123 Health Avenue, Suite 456, New York, NY 10001<br/>" + "Phone: (123) 456-7890<br/>"
				+ "Website: <a href='http://www.jayahospitals.com'>www.jayahospitals.com</a>";
	}

	@Override
	public void sendAppointmentConfirmation(String email, String firstName, String lastName, String date, String time,
			String doctorName, String specialty, int amount) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject("Appointment Confirmation with Dr. " + doctorName);

        // Get the email content with dynamic values
        String content = getEmailTemplate(firstName, lastName, date, time, doctorName, specialty, amount);
        helper.setText(content, true);  // true indicates the content is HTML

        mailSender.send(message);
		
	}

}
