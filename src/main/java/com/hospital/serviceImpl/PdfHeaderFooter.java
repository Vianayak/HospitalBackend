package com.hospital.serviceImpl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class PdfHeaderFooter extends PdfPageEventHelper {
    private final String patientName;
    private final String patientEmail;
    private final String phoneNumber;
    private final String doctorEmail;
    private static final String LOGO_PATH = "C:/hospital_pdfs/logo.png"; // Your local logo path

    public PdfHeaderFooter(String patientName, String patientEmail, String phoneNumber, String doctorEmail) {
        this.patientName = patientName;
        this.patientEmail = patientEmail;
        this.phoneNumber = phoneNumber;
        this.doctorEmail = doctorEmail;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
        Font footerFont = new Font(Font.FontFamily.HELVETICA, 9);

        try {
            // **Header Table**
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidths(new float[]{3, 1}); // Adjust width ratio
            headerTable.setTotalWidth(500);
            headerTable.setLockedWidth(true);

            // **Logo (Left)**
            Image logo = Image.getInstance(LOGO_PATH);
            logo.scaleToFit(100, 50);
            PdfPCell logoCell = new PdfPCell(logo);
            logoCell.setBorder(0);
            logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);

            // **Patient Details + Date (Right)**
            PdfPCell detailsCell = new PdfPCell(new Phrase(
                    "Patient Name: " + patientName + 
                    "\nEmail: " + patientEmail + 
                    "\nDate: " + java.time.LocalDate.now(), 
                    new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL)));
            detailsCell.setBorder(0);
            detailsCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

            headerTable.addCell(logoCell);
            headerTable.addCell(detailsCell);

            // **Position Header Properly**
            float headerY = document.top() + 40; // Adjusted top position
            headerTable.writeSelectedRows(0, -1, 50, headerY, cb);

            // **DRAW LINE BELOW HEADER**
            float headerLineY = headerY - 40; // Lower the line below header content
            cb.moveTo(50, headerLineY);
            cb.lineTo(document.getPageSize().getWidth() - 50, headerLineY);
            cb.stroke();

            // **Ensure Content Does Not Overlap Header on Next Pages**
            document.setMargins(50, 50, document.topMargin() + 50, document.bottomMargin());

            // **Footer Table**
            PdfPTable footerTable = new PdfPTable(2);
            footerTable.setWidths(new float[]{1, 1}); // Equal widths
            footerTable.setTotalWidth(500);
            footerTable.setLockedWidth(true);

            PdfPCell footerLeft = new PdfPCell(new Phrase("Doctor Contact: " + phoneNumber, footerFont));
            footerLeft.setBorder(0);

            PdfPCell footerRight = new PdfPCell(new Phrase("Doctor Email: " + doctorEmail, footerFont));
            footerRight.setBorder(0);
            footerRight.setHorizontalAlignment(Element.ALIGN_RIGHT);

            footerTable.addCell(footerLeft);
            footerTable.addCell(footerRight);

            // **Position Footer Properly**
            float footerY = document.bottom() - 20;
            footerTable.writeSelectedRows(0, -1, 50, footerY, cb);

            // **DRAW LINE ABOVE FOOTER**
            float footerLineY = footerY + 10;
            cb.moveTo(50, footerLineY);
            cb.lineTo(document.getPageSize().getWidth() - 50, footerLineY);
            cb.stroke();

            // **Watermark**
            addBackgroundLogo(writer);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addBackgroundLogo(PdfWriter writer) {
        try {
            PdfContentByte canvas = writer.getDirectContentUnder();
            Image backgroundLogo = Image.getInstance(LOGO_PATH);
            backgroundLogo.setAbsolutePosition(150, 250); // Center the watermark
            backgroundLogo.scaleToFit(300, 300);

            // **Set Transparency**
            PdfGState gs = new PdfGState();
            gs.setFillOpacity(0.05f); // 5% opacity for watermark effect
            canvas.saveState();
            canvas.setGState(gs);
            canvas.addImage(backgroundLogo);
            canvas.restoreState();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
