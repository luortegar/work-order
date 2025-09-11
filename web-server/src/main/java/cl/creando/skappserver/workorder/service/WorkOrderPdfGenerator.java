package cl.creando.skappserver.workorder.service;

import cl.creando.skappserver.common.entity.user.User;
import cl.creando.skappserver.common.service.ImageBase64Util;
import cl.creando.skappserver.workorder.entity.WorkOrder;
import cl.creando.skappserver.workorder.entity.WorkOrderPhoto;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
@AllArgsConstructor
public class WorkOrderPdfGenerator {

    private final SpringTemplateEngine templateEngine;


    public InputStream generateWorkOrderPdf(WorkOrder workOrder) throws IOException {
        Map<String, Object> data = new HashMap<>();

        // === Encabezado ===
        data.put("logo", ImageBase64Util.getImageAsBase64("thymeleaf-templates/img/logo_ingeros.jpg"));
        data.put("workOrderNumber", workOrder.getWorkOrderNumber());
        data.put("companyName", workOrder.getBranch() != null && workOrder.getBranch().getClient() != null
                ? workOrder.getBranch().getClient().getCompanyName()
                : "N/A");
        data.put("branchLocation", workOrder.getBranch() != null ? workOrder.getBranch().getAddress() : "N/A");

        // === Cliente (destinatario) ===
        User recipient = workOrder.getRecipient();
        if (recipient != null) {
            data.put("clientName", recipient.getFullName());
            data.put("clientPhone", recipient.getPhone() != null ? recipient.getPhone() : "—");
            data.put("clientEmail", recipient.getEmail() != null ? recipient.getEmail() : "—");
        } else {
            data.put("clientName", "N/A");
            data.put("clientPhone", "N/A");
            data.put("clientEmail", "N/A");
        }

        // === Fechas y tiempos ===
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy HH:mm", new Locale("es", "CL"));
        String start = workOrder.getStartTime() != null ? workOrder.getStartTime().format(fmt) : "—";
        String end = workOrder.getEndTime() != null ? workOrder.getEndTime().format(fmt) : "—";

        data.put("startDateTime", start);
        data.put("endDateTime", end);

        if (workOrder.getStartTime() != null && workOrder.getEndTime() != null) {
            long hours = Duration.between(workOrder.getStartTime(), workOrder.getEndTime()).toHours();
            data.put("laborHours", hours + " hrs.");
        } else {
            data.put("laborHours", "—");
        }

        // Por ahora dejamos las horas de traslado fijo, o puedes calcularlas de otra tabla
        data.put("travelHours", "—");

        // === Detalle del servicio ===
        List<String> serviceDetails = new ArrayList<>();
        if (workOrder.getServiceDetails() != null) {
            // Si viene como texto plano separado por saltos de línea
            serviceDetails.addAll(Arrays.asList(workOrder.getServiceDetails().split("\\r?\\n")));
        }
        data.put("serviceDetails", serviceDetails);

        // === Observaciones ===
        List<String> observations = new ArrayList<>();
        if (workOrder.getObservations() != null) {
            observations.addAll(Arrays.asList(workOrder.getObservations().split("\\r?\\n")));
        }
        data.put("observations", observations);

        // === Firmas ===
        List<Map<String, String>> signatures = new ArrayList<>();
        if (workOrder.getTechnician() != null) {
            signatures.add(Map.of(
                    "name", workOrder.getTechnician().getFullName(),
                    "contact", workOrder.getTechnician().getEmail() + " — " + workOrder.getTechnician().getPhone()
            ));
        }
        if (recipient != null) {
            signatures.add(Map.of(
                    "name", recipient.getFullName(),
                    "contact", recipient.getEmail() + " — " + recipient.getPhone()
            ));
        }
        data.put("signatures", signatures);

        // === Footer de empresa ===
        data.put("companyFooterLine1", "Mantenimiento, reparación, venta de equipos radiográficos y sistema RISPACS.");
        data.put("companyFooterLine2", "INGEROS SPA. - www.ingeros.cl");

        // === Fotos ===
        List<String> photoList = new ArrayList<>();
        if (workOrder.getFileList() != null) {
            for (WorkOrderPhoto photo : workOrder.getFileList()) {
                // if (photo.getFile() != null && photo.getFile().getPath() != null) {
                //     photoList.add(ImageBase64Util.getImageAsBase64(photo.getFile().getPath()));
                // }
            }
        }
        data.put("photoList", photoList);

        // === Renderizar con Thymeleaf ===
        Context context = new Context();
        context.setVariable("data", data);

        String htmlContent = templateEngine.process("workOrderTemplate", context);

        // === Convertir HTML a PDF ===
        byte[] pdfBytes;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(htmlContent, null);
            builder.toStream(os);
            builder.run();
            pdfBytes = os.toByteArray();
        }

        return new ByteArrayInputStream(pdfBytes);
    }


    public InputStream generateWorkOrderPdfOld(WorkOrder workOrder) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        Map<String, Object> data = new HashMap<>();

        // Encabezado
        data.put("logo", ImageBase64Util.getImageAsBase64("thymeleaf-templates/img/logo_ingeros.jpg"));
        data.put("workOrderNumber", "OT967");
        data.put("companyName", "TraumaCenter");
        data.put("branchLocation", "Rancagua");

        // Cliente
        data.put("clientName", "Angela Mazo Varas");
        data.put("clientPhone", "+569 9042 4283");
        data.put("clientEmail", "angelamazov@gmail.com");

        // Fechas y tiempos
        data.put("startDateTime", "13:00 del 09 de Abril de 2025");
        data.put("endDateTime", "17:00 del 09 de Abril de 2025");
        data.put("laborHours", "4 hrs.");
        data.put("travelHours", "3 hrs.");

        // Detalle del servicio (lista dinámica)
        data.put("serviceDetails", List.of(
                "Se realiza limpieza interna y externa de CPU NX.",
                "Revisión de servicio y log de eventos.",
                "Se realiza limpieza interna y externa de UPS.",
                "NX 24.0.2900 HP Flex Pro i58500 SN: CZC2267QYV",
                "XD14 VIVIX-S 3643VW SN: VEABCB075",
                "UPS SRVPM1KIL SN: 9S1936A78864 SRV36BP-9A SN:9S1917A29174"
        ));

        // Observaciones (lista dinámica)
        data.put("observations", List.of(
                "Se realizan pruebas de disparo satisfactoriamente.",
                "Se reemplaza cable de energía de Cargador de Baterías del Panel.",
                "TM Patricia observa alineación de colimador respecto a EasyLift; se requiere escalera y herramientas para ajustar estructura. Se coordinará visita para ajustes."
        ));

        // Firmas
        data.put("signatures", List.of(
                Map.of("name", "Patricia Pérez", "contact", "+569 9545 2854"),
                Map.of("name", "Rubén Lorca", "contact", "rlorca@in.geros.cl — +569 3434 8370")
        ));

        // Contacto de empresa
        data.put("companyFooterLine1", "Mantenimiento, reparación, venta de equipos radiográficos y sistema RISPACS.");
        data.put("companyFooterLine2", "INGEROS SPA. - www.ingeros.cl");

        // Galería de fotos
        data.put("photoList", List.of(
                Objects.requireNonNull(ImageBase64Util.getImageAsBase64("thymeleaf-templates/img/fierros.jpg")),
                Objects.requireNonNull(ImageBase64Util.getImageAsBase64("thymeleaf-templates/img/fierros.jpg")),
                Objects.requireNonNull(ImageBase64Util.getImageAsBase64("thymeleaf-templates/img/fierros.jpg")),
                Objects.requireNonNull(ImageBase64Util.getImageAsBase64("thymeleaf-templates/img/fierros.jpg")),
                Objects.requireNonNull(ImageBase64Util.getImageAsBase64("thymeleaf-templates/img/fierros.jpg")),
                Objects.requireNonNull(ImageBase64Util.getImageAsBase64("thymeleaf-templates/img/fierros.jpg"))
        ));

        // 2. Create a Thymeleaf context and add the map under the key "data"
        Context context = new Context();
        context.setVariable("data", data);

        // 3. Process the HTML template with Thymeleaf
        String htmlContent = templateEngine.process("workOrderTemplate", context);


        // 3. Convertir el HTML a PDF usando OpenHTMLToPDF
        byte[] pdfBytes;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(htmlContent, null); // null como base URI
            builder.toStream(os);
            builder.run();
            pdfBytes = os.toByteArray();
        }

        return new ByteArrayInputStream(pdfBytes);
    }
}
