package cl.creando.skappserver.common.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class ImageBase64Util {

    public static String getImageAsBase64(String path) throws IOException {
        // Usa ClassPathResource para encontrar el archivo en el classpath
        ClassPathResource imgResource = new ClassPathResource(path);

        // Valida si el archivo existe
        if (!imgResource.exists()) {
            System.err.println("Error: El archivo no se encontró en la ruta: " + path);
            return null;
        }

        // Obtiene la ruta absoluta del archivo para validar el tipo de contenido
        Path filePath = Paths.get(imgResource.getURI());

        // Valida si el archivo es una imagen usando la API de Files
        String mimeType = Files.probeContentType(filePath);
        if (mimeType == null || !mimeType.startsWith("image/")) {
            System.err.println("Error: El archivo no es una imagen válida. Tipo encontrado: " + mimeType);
            return null;
        }

        // Lee los bytes del archivo
        byte[] bytes = StreamUtils.copyToByteArray(imgResource.getInputStream());

        // Codifica los bytes a Base64
        String base64String = Base64.getEncoder().encodeToString(bytes);

        // Retorna el string Base64 completo con el prefijo
        return "data:" + mimeType + ";base64," + base64String;
    }
}
