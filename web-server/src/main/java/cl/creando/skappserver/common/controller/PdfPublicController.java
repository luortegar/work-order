package cl.creando.skappserver.common.controller;

import cl.creando.skappserver.common.service.ImageBase64Util;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/public/v1/pfdExample")
@AllArgsConstructor
public class PdfPublicController {

    private final SpringTemplateEngine templateEngine;

    @GetMapping
    public ResponseEntity<ByteArrayResource> downloadPdf() {
        try {

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

            // 4. Configurar la respuesta HTTP para la descarga
            ByteArrayResource resource = new ByteArrayResource(pdfBytes);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ejemplo.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(pdfBytes.length)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);

        } catch (IOException e) {
            // Manejo de errores de IO
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}