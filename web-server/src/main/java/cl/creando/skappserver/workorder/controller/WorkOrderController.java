package cl.creando.skappserver.workorder.controller;


import cl.creando.skappserver.workorder.entity.WorkOrderStatus;
import cl.creando.skappserver.workorder.request.WorkOrderRequest;
import cl.creando.skappserver.workorder.service.WorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/private/v1/work-orders")
public class WorkOrderController {

    @Autowired
    private WorkOrderService workOrderService;

    @GetMapping
    public ResponseEntity<?> list(
            @RequestParam(name = "searchTerm", required = false, defaultValue = "") String searchTerm,
            @RequestParam(name = "workOrderStatus", required = false) WorkOrderStatus workOrderStatus,
            Pageable pageable) {
        return workOrderService.findAll(searchTerm, workOrderStatus, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> view(@PathVariable UUID id) {
        return ResponseEntity.ok(workOrderService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Object> createWorkOrder(@RequestBody WorkOrderRequest workOrderRequest) {
        return ResponseEntity.ok(workOrderService.save(workOrderRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateWorkOrder(@PathVariable UUID id, @RequestBody WorkOrderRequest workOrderRequest) {
        return ResponseEntity.ok(workOrderService.updateWorkOrder(id, workOrderRequest));
    }

    @PatchMapping("/{id}/work-order-photo")
    public ResponseEntity<Object> updatePhotoToWorkOrder(@PathVariable UUID id, @RequestParam("file") MultipartFile request) {
        return ResponseEntity.ok(workOrderService.updatePhotoToWorkOrder(id, request));
    }

    @GetMapping("/{id}/work-order-photo")
    public ResponseEntity<Object> viewPhotosOfAWorkOrder(@PathVariable UUID id) {
        return ResponseEntity.ok(workOrderService.viewPhotosOfAWorkOrder(id));
    }

    @DeleteMapping("/{id}/work-order-photo/{workOrderPhotoId}")
    public ResponseEntity<Object> deletePhotoOfAWorkOrder(@PathVariable UUID id, @PathVariable UUID workOrderPhotoId) {
        return ResponseEntity.ok(workOrderService.deletePhotoToWorkOrder(id, workOrderPhotoId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable UUID id) {
        return ResponseEntity.ok(workOrderService.deleteById(id));
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<Resource> viewPDF(@PathVariable UUID id) throws IOException {
        return workOrderService.generatePDFDownload(id);
    }

}
