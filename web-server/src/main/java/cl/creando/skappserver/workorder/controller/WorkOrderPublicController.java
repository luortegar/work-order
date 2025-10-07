package cl.creando.skappserver.workorder.controller;


import cl.creando.skappserver.workorder.service.WorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/public/v1/work-orders")
public class WorkOrderPublicController {

    @Autowired
    private WorkOrderService workOrderService;

    @GetMapping("/{id}/ot.pdf")
    public ResponseEntity<Resource> viewPDF(@PathVariable UUID id) throws IOException {
        return workOrderService.generatePDFPreview(id);
    }

}
