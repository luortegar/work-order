package cl.creando.skappserver.workorder.service;

import cl.creando.skappserver.common.service.ImageBase64Util;
import cl.creando.skappserver.workorder.entity.WorkOrder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;


@Service
@AllArgsConstructor
public class WorkOrderPdfGenerator {

    private final SpringTemplateEngine templateEngine;

    public InputStream generateWorkOrderPdf(WorkOrder workOrder) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // 1. Create a map to hold the data
        Map<String, Object> data = new HashMap<>();
        data.put("title", "Reporte de Ejemplo");
        data.put("logo", ImageBase64Util.getImageAsBase64("thymeleaf-templates/img/logo_ingeros.jpg"));
        data.put("date", LocalDate.now());
        data.put("reportId", UUID.randomUUID().toString().substring(0, 8));
        data.put("content", "Este es un contenido de ejemplo para el reporte. El PDF se generará a partir de una plantilla HTML de Thymeleaf.");
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
