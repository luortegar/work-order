package cl.creando.skappserver.workorder.service;

import cl.creando.skappserver.workorder.entity.WorkOrder;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;


@Service
@AllArgsConstructor
public class WorkOrderPdfGenerator {



    public InputStream generateWorkOrderPdf(WorkOrder workOrder) throws IOException {
        // Create a ByteArrayOutputStream to write PDF data to memory
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // Initialize PdfWriter to write to the ByteArrayOutputStream
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Set font (optional, for better readability)
        document.setFont(PdfFontFactory.createFont("Helvetica"));

        // Title
        document.add(new Paragraph("Orden de trabajo de servicios profesionales")
                .setFontSize(16)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.BLUE));

        // Create a table with 2 columns for details
        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();

        // Client and details
        table.addCell(createCell("Cliente:", workOrder.getBranch().getBranchName()));
        table.addCell(createCell("Rut:", workOrder.getBranch().getClient().getUniqueTaxpayerIdentification()));

        // Address
        table.addCell(createCell("Dirección:", workOrder.getBranch().getAddress()));
        table.addCell("");

        // Recipient (solicitante)
        table.addCell(createCell("Solicitante:", workOrder.getRecipient().getFirstName() + " " + workOrder.getRecipient().getLastName()));
        table.addCell(createCell("Tel. fijo:", workOrder.getRecipient().getPhoneNumber()));

        // Equipment details
        table.addCell(createCell("Equipo Model:", workOrder.getEquipment().getEquipmentModel()));
        table.addCell(createCell("Nro. serie:", workOrder.getEquipment().getSerialNumber()));
        table.addCell(createCell("Marca eq.:", workOrder.getEquipment().getEquipmentBrand()));
        table.addCell(createCell("Tipo eq.:", workOrder.getEquipment().getEquipmentType().getTypeName()));

        // Technician
        table.addCell(createCell("Técnico ejecutor 1:", workOrder.getTechnician().getFirstName() + " " + workOrder.getTechnician().getLastName()));

        // Add the table to the document
        document.add(table);

        // Add time information
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        Table timeTable = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
        timeTable.addCell(createCell("Hora Inicio:", workOrder.getStartTime().format(formatter)));
        timeTable.addCell(createCell("Hora Término:", workOrder.getEndTime().format(formatter)));
        document.add(timeTable);

        // Add the total service and other details (customize as needed)
        document.add(new Paragraph("Total Servicio: " + workOrder.getServiceDetails()));

        // Add any observations (informes)
        document.add(new Paragraph("Informe: " + workOrder.getObservations()));

        // Close the document
        document.close();

        // Return an InputStream from the byte array
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }



    // Utility method to create table cells
    private Cell createCell(String header, String content) {
        Cell cell = new Cell();
        cell.add(new Paragraph(header).setBold());
        cell.add(new Paragraph(content != null ? content : ""));
        return cell;
    }

}
