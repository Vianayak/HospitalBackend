package com.hospital.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.hospital.dto.EPrescriptionDto;
import com.hospital.model.BookAppointment;
import com.hospital.model.DateAndTimeInfo;
import com.hospital.model.DoctorsInfo;
import com.hospital.model.MeetingDetails;
import com.hospital.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void sendOTPEmail(String email, String otp) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		helper.setTo(email);
		helper.setSubject("Your OTP Code - TechSpryn");

		// Get the email content with dynamic values
		String content = getOtpEmailTemplate(otp);
		helper.setText(content, true); // true indicates the content is HTML

		mailSender.send(message);
	}
	
	
	public String getOtpEmailTemplate(String otp) {
	    return "Hi, <br/><br/>"
	            + "Your OTP is: <b>" + otp + "</b><br/><br/>"
	            + "This OTP is valid for 5 minutes.<br/><br/>"
	            + "If you did not request this OTP, please disregard this email.<br/><br/>"
	            + "Best regards,<br/>"
	            + "The TechSpryn Team<br/><br/>"
	            + "TechSpryn<br/>"
	            + "123 Health Avenue, Suite 456, New York, NY 10001<br/>"
	            + "Phone: (123) 456-7890<br/>"
	            + "Website: <a href='http://www.TechSpryn.com'>www.TechSpryn.com</a>";
	}

	public String getEmailTemplate(String firstName, String lastName, String date, String time, String doctorName,
			String specialty, int amount) {
		return "Hello " + firstName + " " + lastName + ",<br/><br/>"
				+ "Thank you for booking an appointment with us! Here are the details of your upcoming visit:<br/><br/>"
				+ "<b>Appointment Details</b><br/>" + "Date: " + date + "<br/>" + "Time: " + time + "<br/>" + "Doctor: "
				+ doctorName + "<br/>" + "Specialty: " + specialty + "<br/>" + "Clinic Name: TechSpryn<br/>"
				+ "Location: 123 Health Avenue, Suite 456, New York, NY 10001<br/><br/>" + "<b>Payment Details</b><br/>"
				+ "Payment Status: Paid<br/>" + "Amount Paid: " + amount + "<br/>"
				+ "Please arrive 10 minutes early for your appointment. Bring any previous medical records if applicable.<br/><br/>"
				+ "<b>Contact Information</b><br/>"
				+ "For any queries, contact us at (123) 456-7890 or support@heartcareclinic.com.<br/><br/>"
				+ "Looking forward to serving you,<br/>" + "TechSpryn Team<br/><br/>" + "TechSpryn<br/>"
				+ "123 Health Avenue, Suite 456, New York, NY 10001<br/>" + "Phone: (123) 456-7890<br/>"
				+ "Website: <a href='http://www.TechSpryn.com'>www.TechSpryn.com</a>";
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
		helper.setText(content, true); // true indicates the content is HTML

		mailSender.send(message);

	}

	public String getDoctorEmailTemplate(MeetingDetails meet, BookAppointment app, DoctorsInfo doctor,
			DateAndTimeInfo info, String doctorURL) {
		return "Hello Dr. " + doctor.getName() + ",<br/><br/>"
				+ "You have an upcoming appointment with the following details:<br/><br/>"
				+ "<b>Appointment Details</b><br/>" + "Patient: " + app.getFirstName() + " " + app.getLastName()
				+ "<br/>" + "Date: " + info.getDate() + "<br/>" + "Time: " + info.getTime() + "<br/>" + "Specialty: "
				+ doctor.getSpecialization() + "<br/>" + "Clinic Name: TechSpryn<br/>"
				+ "Location: 123 Health Avenue, Suite 456, New York, NY 10001<br/><br/>" + "<b>Meeting Details</b><br/>"
				+ "Meeting Link: <a href='" + doctorURL + "'>" + doctorURL + "</a><br/>"
				+ "Please ensure you join the meeting on time.<br/><br/>"
				+ "For any assistance, please contact our support team.<br/><br/>"
				+ "Thank you for being a part of TechSpryn.<br/><br/>" + "TechSpryn Team<br/><br/>"
				+ "TechSpryn<br/>" + "123 Health Avenue, Suite 456, New York, NY 10001<br/>"
				+ "Phone: (123) 456-7890<br/>"
				+ "Website: <a href='http://www.TechSpryn.com'>www.TechSpryn.com</a>";
	}

	public String getPatientEmailTemplate(MeetingDetails meet, BookAppointment app, DoctorsInfo doctor,
			DateAndTimeInfo info, String patientURL) {
		return "Hello " + app.getFirstName() + " " + app.getLastName() + ",<br/><br/>"
				+ "Thank you for booking an appointment with us! Here are the details of your upcoming visit:<br/><br/>"
				+ "<b>Appointment Details</b><br/>" + "Date: " + info.getDate() + "<br/>" + "Time: " + info.getTime() + "<br/>" + "Doctor: "
				+ doctor.getName() + "<br/>" + "Specialty: " + doctor.getSpecialization() + "<br/>" + "Clinic Name: TechSpryn<br/>"
				+ "Location: 123 Health Avenue, Suite 456, New York, NY 10001<br/><br/>" + "<b>Payment Details</b><br/>"
				+ "Payment Status: Paid<br/>" + "Amount Paid: " + app.getAmount() + "<br/>"
				+ "Meeting Id: "+meet.getMeetingRoom()+"<br/><br/>" + "<b>Meeting Details</b><br/>" + "Meeting Link: <a href='"
				+ patientURL + "'>" + patientURL + "</a><br/>"
				+ "Please join the meeting at your scheduled time.<br/><br/>"
				+ "Please arrive 10 minutes early for your appointment. Bring any previous medical records if applicable.<br/><br/>"
				+ "<b>Contact Information</b><br/>"
				+ "For any queries, contact us at (123) 456-7890 or support@TechSpryn.com.<br/><br/>"
				+ "Looking forward to serving you,<br/>" + "TechSpryn Team<br/><br/>" + "TechSpryn<br/>"
				+ "123 Health Avenue, Suite 456, New York, NY 10001<br/>" + "Phone: (123) 456-7890<br/>"
				+ "Website: <a href='http://www.TechSpryn.com'>www.TechSpryn.com</a>";
	}
	
	@Override
	public void sendMeetingToDoctor(MeetingDetails meet, BookAppointment app, DoctorsInfo doctor,
			DateAndTimeInfo info, String doctorURL) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		helper.setTo(doctor.getEmail());
		helper.setSubject("Meeting Details with " + app.getFirstName()+" "+app.getLastName());

		// Get the email content with dynamic values
		String content = getDoctorEmailTemplate(meet,app,doctor,info,doctorURL);
		helper.setText(content, true); // true indicates the content is HTML

		mailSender.send(message);

	}
	
	@Override
	public void sendMeetingToPatient(MeetingDetails meet, BookAppointment app, DoctorsInfo doctor,
			DateAndTimeInfo info, String patientURL) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		helper.setTo(app.getEmail());
		helper.setSubject("Meeting details with Dr. " + doctor.getName());

		// Get the email content with dynamic values
		String content = getPatientEmailTemplate(meet,app,doctor,info,patientURL);
		helper.setText(content, true); // true indicates the content is HTML

		mailSender.send(message);

	}

	public String generateEPrescriptionEmail(String firstName, String lastName, String appointmentDate, String doctorName, String specialization) {
	    return "Hello " + firstName + " " + lastName + ",<br/><br/>"
	        + "We hope this message finds you well. Following your recent consultation, we are providing you with your e-prescription. Please find the attached document for your medication details and doctor's notes.<br/><br/>"
	        + "<b>🩺 Prescription Details</b><br/>"
	        + "- <b>Date of Consultation:</b> " + appointmentDate + "<br/>"
	        + "- <b>Doctor:</b> Dr. " + doctorName + "<br/>"
	        + "- <b>Specialization:</b> " + specialization + "<br/><br/>"
	        + "<b>💊 Medications Prescribed:</b><br/>"
	        + "Please follow the medication schedule as outlined in the attached PDF. Make sure to adhere to the dosage instructions carefully.<br/><br/>"
	        + "<b>📍 Clinic Information:</b><br/>"
	        + "TechSpryn<br/>"
	        + "123 Health Avenue, Suite 456, New York, NY 10001<br/>"
	        + "Phone: (123) 456-7890<br/>"
	        + "Website: <a href='http://www.TechSpryn.com'>www.TechSpryn.com</a><br/><br/>"
	        + "<b>🔔 Important Reminders:</b><br/>"
	        + "- Please complete your medication course as prescribed.<br/>"
	        + "- Contact us immediately if you experience any side effects.<br/>"
	        + "- Follow-up consultations can be scheduled via our online portal.<br/><br/>"
	        + "If you have any questions or need assistance, feel free to contact us at (123) 456-7890 or email us at support@techspryn.com.<br/><br/>"
	        + "Wishing you a speedy recovery,<br/>"
	        + "<b>The TechSpryn Team</b><br/><br/>";
	}
	
	
	@Override
	public void sendEPrescriptionEmailToPtient(EPrescriptionDto dto, byte[] pdfAttachment) throws MessagingException {
	    MimeMessage message = mailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message, true);

	    helper.setTo(dto.getPatientEmail());
	    helper.setSubject("Your E-Prescription from TechSpryn");
	    helper.setText(generateEPrescriptionEmail(dto.getFirstName(), dto.getLastName(), dto.getAppointmentDate(), dto.getDoctorName(), dto.getSpecialization()), true); // Enable HTML content

	    // Attach PDF
	    helper.addAttachment("E-Prescription.pdf", new ByteArrayResource(pdfAttachment));

	    mailSender.send(message);
	}
	
	
	
	public String getNurseRegistrationEmailTemplate(String nurseName, String googleFormLink) {
	    return "Hello " + nurseName + ",<br/><br/>"
	            + "Welcome to TechSpryn! We are excited to have you on board as a nurse.<br/><br/>"
	            + "To complete your registration, please fill out the following form:<br/><br/>"
	            + "<b>Complete Your Registration</b><br/>"
	            + "<a href='" + googleFormLink + "'>" + googleFormLink + "</a><br/><br/>"
	            + "This form will help us gather additional details required for your profile.<br/><br/>"
	            + "<b>Next Steps:</b><br/>"
	            + "1. Click the link above and fill in your details.<br/>"
	            + "2. Once submitted, our team will verify your information.<br/>"
	            + "3. You will receive confirmation once your profile is fully set up.<br/><br/>"
	            + "<b>Contact Support</b><br/>"
	            + "If you have any questions, feel free to reach out to us at support@techspryn.com.<br/><br/>"
	            + "Best Regards,<br/>"
	            + "TechSpryn Team<br/><br/>"
	            + "TechSpryn<br/>"
	            + "123 Health Avenue, Suite 456, New York, NY 10001<br/>"
	            + "Phone: (123) 456-7890<br/>"
	            + "Website: <a href='http://www.TechSpryn.com'>www.TechSpryn.com</a>";
	}


	public void sendGoogleFormEmail(String name, String email) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setTo(email);
			helper.setSubject("Complete Your Registration - TechSpryn");

			helper.setText(getNurseRegistrationEmailTemplate(name, "https://forms.gle/NAFGvm3Tmy98ANRv7"));

	        mailSender.send(message);

	        System.out.println("Email sent successfully to: " + email);
	    } catch (Exception e) {
	        System.err.println("Error sending email: " + e.getMessage());
	    }
	}

	
	
}

