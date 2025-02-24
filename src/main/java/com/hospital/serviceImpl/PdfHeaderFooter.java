package com.hospital.serviceImpl;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

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

        PdfPTable headerTable = new PdfPTable(3);
        headerTable.setTotalWidth(500);
        headerTable.setLockedWidth(true);

        try {
            // **Small Logo in Header**
            Image headerLogo = Image.getInstance("C:/hospital_pdfs/logo.png");
            headerLogo.scaleToFit(100, 100);
            PdfPCell logoCell = new PdfPCell(headerLogo);
            logoCell.setBorder(0);
            logoCell.setPadding(5);

            // **Left: Patient Details**
            PdfPCell leftCell = new PdfPCell(new Phrase("Patient Name: " + patientName + "\nEmail: " + patientEmail, headerFont));
            leftCell.setBorder(0);

            // **Right: Date**
            PdfPCell rightCell = new PdfPCell(new Phrase("Date: " + java.time.LocalDate.now(), headerFont));
            rightCell.setBorder(0);
            rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

            headerTable.addCell(logoCell);
            headerTable.addCell(leftCell);
            headerTable.addCell(rightCell);

            headerTable.writeSelectedRows(0, -1, 50, document.top() + 50, cb);

            // **Footer**
            PdfPTable footerTable = new PdfPTable(2);
            footerTable.setTotalWidth(500);
            footerTable.setLockedWidth(true);

            PdfPCell footerLeft = new PdfPCell(new Phrase("Doctor Contact: " + phoneNumber, footerFont));
            footerLeft.setBorder(0);

            PdfPCell footerRight = new PdfPCell(new Phrase("Doctor Email: " + doctorEmail, footerFont));
            footerRight.setBorder(0);
            footerRight.setHorizontalAlignment(Element.ALIGN_RIGHT);

            footerTable.addCell(footerLeft);
            footerTable.addCell(footerRight);
            footerTable.writeSelectedRows(0, -1, 50, document.bottom() - 20, cb); // Positioned at the bottom
            addBackgroundLogo(writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void addBackgroundLogo(PdfWriter writer) {
        try {
            PdfContentByte canvas = writer.getDirectContentUnder();
            Image backgroundLogo = Image.getInstance(LOGO_PATH);
            backgroundLogo.setAbsolutePosition(150, 250); // **Position in the center**
            backgroundLogo.scaleToFit(300, 300); // **Resize for watermark effect**

            // **Set Transparency**
            PdfGState gs = new PdfGState();
            gs.setFillOpacity(0.15f); // **5% opacity for ultra-light effect**
            canvas.saveState();
            canvas.setGState(gs);
            canvas.addImage(backgroundLogo);
            canvas.restoreState();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

