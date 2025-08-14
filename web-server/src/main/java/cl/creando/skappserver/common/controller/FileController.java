package cl.creando.skappserver.common.controller;

import cl.creando.skappserver.common.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/private/v1/files")
@AllArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/{id}")
    public ResponseEntity<?> viewFile(@PathVariable(name = "id") UUID id) {
        return ResponseEntity.ok(fileService.findByFileId(id));
    }

    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fileService.saveFile(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable(name = "id") UUID id) {
        return ResponseEntity.ok(fileService.deleteFile(id));
    }
}
