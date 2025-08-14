package cl.creando.skappserver.common.controller;

import cl.creando.skappserver.common.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/public/v1/files")
@AllArgsConstructor
public class FilePublicController {

    private final FileService fileService;

    @GetMapping("/{id}/download")
    public ResponseEntity<?> downloadFile(@PathVariable(name = "id") UUID id) {
        return fileService.downloadFile(id);
    }

}
