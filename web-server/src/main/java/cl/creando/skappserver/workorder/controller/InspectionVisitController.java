package cl.creando.skappserver.workorder.controller;


import cl.creando.skappserver.workorder.request.InspectionVisitRequest;
import cl.creando.skappserver.workorder.service.InspectionVisitService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/private/v1/inspection-visits")
@AllArgsConstructor
public class InspectionVisitController {

    private final InspectionVisitService inspectionVisitService;

    @GetMapping("/branch-offices/{branchId}")
    public ResponseEntity<?> list(@PathVariable UUID branchId,
            @RequestParam(name = "searchTerm", required = false, defaultValue = "") String searchTerm,
            Pageable pageable) {
        return ResponseEntity.ok(inspectionVisitService.findAll(branchId, searchTerm, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> view(@PathVariable UUID id) {
        return ResponseEntity.ok(inspectionVisitService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody InspectionVisitRequest inspectionVisitRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inspectionVisitService.save(inspectionVisitRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable UUID id, @RequestBody InspectionVisitRequest inspectionVisitRequest) {
        return ResponseEntity.ok(inspectionVisitService.update(id, inspectionVisitRequest));
    }

    @PatchMapping("/{id}/visit-photo")
    public ResponseEntity<Object> updatePhotoToWorkOrder(@PathVariable UUID id, @RequestParam("file") MultipartFile request) {
        return ResponseEntity.ok(inspectionVisitService.saveVisitPhoto(id, request));
    }

    @GetMapping("/{id}/visit-photo")
    public ResponseEntity<Object> viewPhotosOfAWorkOrder(@PathVariable UUID id) {
        return ResponseEntity.ok(inspectionVisitService.viewVisitPhoto(id));
    }

    @DeleteMapping("/{id}/visit-photo/{visitPhotoId}")
    public ResponseEntity<Object> deletePhotoOfAWorkOrder(@PathVariable UUID id, @PathVariable UUID visitPhotoId) {
        return ResponseEntity.ok(inspectionVisitService.deleteVisitPhoto(id, visitPhotoId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable UUID id) {
        return ResponseEntity.ok(inspectionVisitService.deleteById(id));
    }

}
