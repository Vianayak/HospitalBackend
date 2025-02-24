package com.hospital.serviceImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hospital.model.TabletInfo;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class PdfService {

	
	private static final String SAVE_DIRECTORY = "C:/hospital_pdfs/";
    private static final String LOGO_PATH = "C:/hospital_pdfs/logo.png"; // Your local logo path

    public String generatePdf(String patientName, String patientEmail, String phoneNumber, String doctorEmail, List<TabletInfo> tablets) {
        try {
            // Ensure directory exists
            File dir = new File(SAVE_DIRECTORY);
            if (!dir.exists()) dir.mkdirs();

            String filePath = SAVE_DIRECTORY + patientName.replace(" ", "_") + "_prescription.pdf";
            Document document = new Document(PageSize.A4, 50, 50, 80, 50); // Top margin for header, bottom margin for footer
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));

            // Attach custom page event
            PdfHeaderFooter event = new PdfHeaderFooter(patientName, patientEmail, phoneNumber, doctorEmail);
            writer.setPageEvent(event);

            document.open();

            // Add transparent background logo
            PdfContentByte canvas = writer.getDirectContentUnder();
            Image backgroundLogo = Image.getInstance(LOGO_PATH);
            backgroundLogo.setAbsolutePosition(100, 250); // Center the logo
            backgroundLogo.scaleToFit(300, 300); // Resize for watermark effect

            // **Set Transparency**
            PdfGState gs = new PdfGState();
            gs.setFillOpacity(0.05f); // 5% opacity for ultra-light effect
            canvas.saveState();
            canvas.setGState(gs);
            canvas.addImage(backgroundLogo);
            canvas.restoreState();

            // Add spacing after the header
            document.add(new Paragraph("\n\n"));

            // Create the table
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(20);
            table.setSpacingAfter(20);

            // Table headers
            addTableHeader(table, "Tablet Name");
            addTableHeader(table, "Days");
            addTableHeader(table, "Slot");
            addTableHeader(table, "Slot Timing");

            // Table content
            for (TabletInfo tablet : tablets) {
                table.addCell(tablet.getTabName());
                table.addCell(String.valueOf(tablet.getDays()));
                table.addCell(tablet.getSlot());
                table.addCell(tablet.getSlotTiming());
            }

            document.add(table);
            document.close();
            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void addTableHeader(PdfPTable table, String headerTitle) {
        PdfPCell header = new PdfPCell();
        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
        header.setBorderWidth(1);
        header.setPhrase(new Phrase(headerTitle, new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
        table.addCell(header);
    }
}
