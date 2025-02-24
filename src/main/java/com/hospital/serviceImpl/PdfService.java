package com.hospital.serviceImpl;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.hospital.model.TabletInfo;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

@Service
public class PdfService {

    private static final String SAVE_DIRECTORY = "C:/hospital_pdfs/";
    private static final String LOGO_PATH = "C:/hospital_pdfs/logo.png";

    public byte[] generatePdf(String doctorRegNum, String patientRegNum, String doctorNotes, List<TabletInfo> tablets) {
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

                        Font headerFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.RED);
                        Font subHeaderFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
                        Font infoFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

                        // Doctor details
                        ColumnText.showTextAligned(canvas, Element.ALIGN_RIGHT, new Phrase("Dr.VIGNAN NAIK", headerFont), document.right(), document.top() + 80, 0);
                        ColumnText.showTextAligned(canvas, Element.ALIGN_RIGHT, new Phrase("Pediatrics", subHeaderFont), document.right(), document.top() + 60, 0);
                        ColumnText.showTextAligned(canvas, Element.ALIGN_RIGHT, new Phrase("+91(8919967393)", subHeaderFont), document.right(), document.top() + 40, 0);

                        // Patient Information Row
                        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("Patient Name: B.Vinayak", infoFont), document.left(), document.top() - 10, 0);
                        ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, new Phrase("Age: 27", infoFont), (document.right() + document.left()) / 2, document.top() - 10, 0);
                        ColumnText.showTextAligned(canvas, Element.ALIGN_RIGHT, new Phrase("Sex: M", infoFont), document.right(), document.top() - 10, 0);

                        // Line after patient info
                        canvas.moveTo(document.left(), document.top() - 20);
                        canvas.lineTo(document.right(), document.top() - 20);
                        canvas.stroke();

                        // Footer
                        canvas.moveTo(document.left(), document.bottom() - 10);
                        canvas.lineTo(document.right(), document.bottom() - 10);
                        canvas.stroke();
                        BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
                        canvas.beginText();
                        canvas.setFontAndSize(baseFont, 12);
                        canvas.showTextAligned(Element.ALIGN_LEFT, "techspryn@gmail.com", document.left(), document.bottom() - 30, 0);
                        canvas.showTextAligned(Element.ALIGN_RIGHT, "Date: 24-02-2025", document.right(), document.bottom() - 30, 0);
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
                    document.add(new Paragraph(" ")); // Add spacing after new page
                }

                Paragraph tabletDetails = new Paragraph();
                tabletDetails.setSpacingBefore(10f); // Adds space before each entry
                tabletDetails.setSpacingAfter(10f);  // Adds space after each entry
                tabletDetails.add(new Chunk(serial++ + ". ", normalFont));
                tabletDetails.add(new Chunk(tablet.getTabName(), normalFont));
                tabletDetails.add(new Chunk(" - " + tablet.getDays() + " days", normalFont));
                tabletDetails.add(new Chunk(", " + tablet.getSlot(), normalFont));
                tabletDetails.add(new Chunk(", " + tablet.getSlotTiming(), normalFont));
                document.add(tabletDetails);
            }

            // Doctor Notes
            document.add(new Paragraph("Doctor's Notes:", headerFont));
            document.add(new Paragraph(doctorNotes, normalFont));

            document.close();

            // Save and return PDF
            byte[] pdfBytes = out.toByteArray();
            String filePath = "D:/TechSpryn/Generated_PediatricsForm.pdf";
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
