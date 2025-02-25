package com.hospital.controller;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.dto.MeetingResponse;
import com.hospital.model.TabletInfo;
import com.hospital.service.TabletInfoService;
import com.hospital.serviceImpl.PdfService;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

	@RestController
	@RequestMapping("/api/tablets")
	@CrossOrigin(origins = "*")
	public class TabletInfoController {

		@Autowired
		private PdfService pdfService;
		
	    @Autowired
	    private TabletInfoService tabletInfoService;

	    @PostMapping("/saveTablets")
	    public ResponseEntity<?> saveTablets(
	            @RequestParam String doctorRegNum,
	            @RequestParam String patientEmail,
	            @RequestParam String doctorNotes,
	            @RequestBody List<Map<String, Object>> tablets) {
	        try {
	            tabletInfoService.saveTablets(doctorRegNum, patientEmail,doctorNotes, tablets);
	            return ResponseEntity.status(HttpStatus.CREATED).body("saved");
	        } catch (Exception e) {
	            return ResponseEntity.status(500).build();
	        }
	    }
	    
	    @GetMapping("/patientDetails")
	    public ResponseEntity<List<MeetingResponse>> getDoctorMeetings(
	            @RequestParam String email, 
	            @RequestParam String date) {

	        List<MeetingResponse> meetings = tabletInfoService.getDoctorMeetings(date, email);

	        if (meetings.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	        }

	        return ResponseEntity.ok(meetings);
	    }
	    
	    
	    
//	    @PostMapping("/generate")
//	    public ResponseEntity<Resource> generatePdf(@RequestParam String patientName,
//	                                                @RequestParam String patientEmail,
//	                                                @RequestParam String phoneNumber,
//	                                                @RequestParam String doctorEmail,
//	                                                @RequestBody List<TabletInfo> tablets) {
//	        String filePath = pdfService.generatePdf(patientName, patientEmail, phoneNumber, doctorEmail, tablets);
//	        if (filePath == null) {
//	            return ResponseEntity.internalServerError().build();
//	        }
//
//	        try {
//	            Path path = Paths.get(filePath);
//	            Resource resource = new UrlResource(path.toUri());
//
//	            return ResponseEntity.ok()
//	                    .contentType(MediaType.APPLICATION_PDF)
//	                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + path.getFileName().toString())
//	                    .body(resource);
//
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	            return ResponseEntity.internalServerError().build();
//	        }
//	    }

	    
	    @GetMapping("/generate")
	    public ResponseEntity<byte[]> generatePdf() {
	        try {
	            // Create PDF in memory
	            Document document = new Document();
	            ByteArrayOutputStream out = new ByteArrayOutputStream();
	            PdfWriter writer = PdfWriter.getInstance(document, out);
	            document.open();

	            PdfContentByte canvas = writer.getDirectContentUnder();

	         // Add Light Background Image Using PdfGState for Transparency
	            Image background = Image.getInstance("D:\\TechSpryn\\TechSpryn_New.png"); // Path to background image
	            background.setAbsolutePosition(50, 200); // Position it nicely on the page
	            background.scaleAbsolute(400, 400); // Scale for better coverage

	            // Apply Transparency Using PdfGState
	            PdfGState gs = new PdfGState();
	            gs.setFillOpacity(0.50f); // Set to 5% opacity for a light watermark effect
	            canvas.saveState();
	            canvas.setGState(gs);
	            canvas.addImage(background);
	            canvas.restoreState(); // Reset the state so it doesn't affect other elements



	            
	         // Add Logo Above Name Row without Overlap
	            Image logo = Image.getInstance("D:\\\\TechSpryn\\\\TechSpryn_New.png"); // Correct path
	            logo.scaleToFit(300, 250); // Reduce size slightly
	            logo.setAbsolutePosition(-20, 650); // Move it higher to prevent overlapping
	            document.add(logo);




	            // Add Header Details
	            Font headerFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.RED);
	            Font subHeaderFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
	            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12,Font.BOLD);

	            Paragraph doctorName = new Paragraph("Dr.VIGNAN NAIK", headerFont);
	            doctorName.setAlignment(Element.ALIGN_RIGHT);
	            document.add(doctorName);

	            Paragraph specialization = new Paragraph("Pediatrics", subHeaderFont);
	            specialization.setAlignment(Element.ALIGN_RIGHT);
	            document.add(specialization);

	            Paragraph contact = new Paragraph("+91(8919967393)", normalFont);
	            contact.setAlignment(Element.ALIGN_RIGHT);
	            document.add(contact);

	         // Add Patient Information in a Table for Better Spacing
	            PdfPTable patientInfoTable = new PdfPTable(3); // 3 columns for Name, Age, and Sex
	            patientInfoTable.setWidthPercentage(100); // Stretch from left to right
	            patientInfoTable.setSpacingBefore(20); // Add some space before the table
	            patientInfoTable.setWidths(new float[]{5, 2, 1}); // Wider space for the Name column

	            Font subHeaderFont1 = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

	            // Add cells with proper alignment
	            PdfPCell nameCell = new PdfPCell(new Phrase("Patient Name: B.Vinayak", subHeaderFont1));
	            nameCell.setBorder(0);
	            nameCell.setHorizontalAlignment(Element.ALIGN_LEFT);

	            PdfPCell ageCell = new PdfPCell(new Phrase("Age: 27", subHeaderFont1));
	            ageCell.setBorder(0);
	            ageCell.setHorizontalAlignment(Element.ALIGN_CENTER);

	            PdfPCell sexCell = new PdfPCell(new Phrase("Sex: M", subHeaderFont1));
	            sexCell.setBorder(0);
	            sexCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

	            // Add cells to the table
	            patientInfoTable.addCell(nameCell);
	            patientInfoTable.addCell(ageCell);
	            patientInfoTable.addCell(sexCell);

	         // Add the table to the document
	         // Add the table to the document
	            document.add(patientInfoTable);

	            // Draw a horizontal underline after the patient information row
	            PdfContentByte lineCanvas = writer.getDirectContent();
	            lineCanvas.setLineWidth(1f); // Set line thickness

	            // Get the Y-position after adding the table
	            float yPosition = writer.getVerticalPosition(true) - 5;

	            // Draw a horizontal line from left to right across the page
	            lineCanvas.moveTo(document.left(), yPosition); // Start of the line
	            lineCanvas.lineTo(document.right(), yPosition); // End of the line
	            lineCanvas.stroke();


	            
	            canvas.moveTo(50, 50); // X-start, Y-position before footer text
	            canvas.lineTo(550, 50); // X-end, same Y
	            canvas.stroke();


	            PdfContentByte footerCanvas = writer.getDirectContent();
	            BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
	            footerCanvas.beginText();
	            footerCanvas.setFontAndSize(baseFont, 12);

	            // Email at bottom-left
	            footerCanvas.showTextAligned(Element.ALIGN_LEFT, "techspryn@gmail.com", 50, 30, 0);

	            // Date at bottom-right
	            footerCanvas.showTextAligned(Element.ALIGN_RIGHT, "Date: 24-02-2025", document.right() - 50, 30, 0);

	            footerCanvas.endText();

	            document.close();

	            // Return PDF as response
	            byte[] pdfBytes = out.toByteArray();
	            HttpHeaders headers = new HttpHeaders();
	            headers.setContentType(MediaType.APPLICATION_PDF);
	            headers.setContentDispositionFormData("attachment", "PediatricsForm.pdf");
	            return ResponseEntity.ok().headers(headers).body(pdfBytes);

	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(500).build();
	        }
	    }
}
