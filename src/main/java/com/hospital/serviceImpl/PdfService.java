package com.hospital.serviceImpl;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.dto.UsersDto;
import com.hospital.model.DoctorsInfo;
import com.hospital.model.SlotAndTimeModel;
import com.hospital.model.TabletInfo;
import com.hospital.repo.SlotAndTimeRepo;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class PdfService {

    private static final String SAVE_DIRECTORY = "C:/hospital_pdfs/";
    private static final String LOGO_PATH = "C:/hospital_pdfs/logo.png";

    @Autowired
    private SlotAndTimeRepo slotRepo;
    
    
    public byte[] generatePdf(DoctorsInfo doctor, UsersDto patientDetails, int age, char sex, String doctorNotes, List<TabletInfo> tablets,String pdfNumb) {
    	
    	// Get today's date
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        // Define the formatter for the desired pattern
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

        // Format the date
        String formattedDate = today.format(formatter);
        String formattedTime = now.format(timeFormatter);

        try {
            Document document = new Document(PageSize.A4, 50, 50, 150, 80); // Increased top margin for header
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, out);

            // Attach header and footer events
            writer.setPageEvent(new PdfPageEventHelper() {
                @Override
                public void onEndPage(PdfWriter writer, Document document) {
                    try {
                        PdfContentByte canvas = writer.getDirectContent();
                        Image logo = Image.getInstance(LOGO_PATH);
                        logo.scaleToFit(300, 250);
                        logo.setAbsolutePosition(-20, 650);
                        canvas.addImage(logo);
                        
                        Image background = Image.getInstance(LOGO_PATH); // Path to background image
        	            background.setAbsolutePosition(50, 200); // Position it nicely on the page
        	            background.scaleAbsolute(400, 400); // Scale for better coverage
        	            PdfGState gs = new PdfGState();
        	            gs.setFillOpacity(0.50f); // Set to 5% opacity for a light watermark effect
        	            canvas.saveState();
        	            canvas.setGState(gs);
        	            canvas.addImage(background);
        	            canvas.restoreState(); // Reset the state so it doesn't affect other elements

                        Font headerFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.RED);
                        Font subHeaderFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
                        Font infoFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

                        // Doctor details
                        ColumnText.showTextAligned(canvas, Element.ALIGN_RIGHT, new Phrase("Dr."+doctor.getName(), headerFont), document.right(), document.top() + 80, 0);
                        ColumnText.showTextAligned(canvas, Element.ALIGN_RIGHT, new Phrase(doctor.getSpecialization(), subHeaderFont), document.right(), document.top() + 60, 0);
                        ColumnText.showTextAligned(canvas, Element.ALIGN_RIGHT, new Phrase("+91(8919967393)", subHeaderFont), document.right(), document.top() + 40, 0);

                     // Calculate available width
                        float totalWidth = document.right() - document.left();
                        float unitWidth = totalWidth / 7; // 5 + 1 + 1 = 7 units

                        // Set positions based on the ratio 5:1:1
                        float nameX = document.left();                     // Starts from the left
                        float ageX = document.left() + unitWidth * 5;      // Starts after 5 units
                        float sexX = document.left() + unitWidth * 6;      // Starts after 6 units

                        // Draw Patient Information Row
                        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("Patient Name: "+patientDetails.getFirstName()+" "+patientDetails.getLastName(), infoFont), nameX, document.top() - 10, 0);
                        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("Age: "+age, infoFont), ageX, document.top() - 10, 0);
                        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("Sex: "+sex, infoFont), sexX, document.top() - 10, 0);



                        // Line after patient info
                        canvas.moveTo(document.left(), document.top() - 20);
                        canvas.lineTo(document.right(), document.top() - 20);
                        canvas.stroke();

                        // Footer
                        canvas.moveTo(document.left(), document.bottom() - 10);
                        canvas.lineTo(document.right(), document.bottom() - 10);
                        canvas.stroke();
                        BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
                        canvas.beginText();
                        canvas.setFontAndSize(baseFont, 12);
                        canvas.showTextAligned(Element.ALIGN_LEFT, "techspryn@gmail.com", document.left(), document.bottom() - 30, 0);
                        canvas.showTextAligned(Element.ALIGN_LEFT, pdfNumb, document.left(), document.bottom() - 45, 0);
                        canvas.showTextAligned(Element.ALIGN_RIGHT, "Date: "+formattedDate, document.right(), document.bottom() - 30, 0);
                        canvas.showTextAligned(Element.ALIGN_RIGHT, "Time: "+formattedTime, document.right(), document.bottom() - 45, 0);
                        canvas.endText();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            document.open();

            // Tablet Information Section
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.RED);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12);
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Tablet Information:", headerFont));

            int serial = 1;
            for (TabletInfo tablet : tablets) {
                if (writer.getVerticalPosition(true) < 150) {
                    document.newPage();
                    document.add(new Paragraph(" "));
                }

                Paragraph tabletDetails = new Paragraph();
                tabletDetails.setSpacingBefore(10f);
                tabletDetails.setSpacingAfter(10f);
                tabletDetails.add(new Chunk(serial++ + ". ", normalFont));
                tabletDetails.add(new Chunk(tablet.getTabName(), normalFont));
                tabletDetails.add(new Chunk(" - " + tablet.getDays() + " days", normalFont));

                // Fetch slots and timings from SlotAndTimeModel
                List<SlotAndTimeModel> slots = slotRepo.findByTabletId(tablet.getId());
                for (SlotAndTimeModel slot : slots) {
                    tabletDetails.add(new Chunk(" ( " + slot.getSlot() + " - " + slot.getTiming() + " )", normalFont));
                }

                document.add(tabletDetails);
            }


            // Doctor Notes
            document.add(new Paragraph("Doctor's Notes:", headerFont));
            document.add(new Paragraph(doctorNotes, normalFont));

            document.close();

            
            String patientRegNum = patientDetails.getRegistrationNumber(); // Assuming getter exists
            String doctorRegNum = doctor.getRegestrationNum(); // Assuming getter exists
            String fileName = patientRegNum + "-" + doctorRegNum + ".pdf";
            
            
            // Save and return PDF
            byte[] pdfBytes = out.toByteArray();
            String filePath = SAVE_DIRECTORY+fileName;
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(pdfBytes);
                fos.flush();
                System.out.println("PDF saved successfully at " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return pdfBytes;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
