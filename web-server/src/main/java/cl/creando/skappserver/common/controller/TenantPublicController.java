package cl.creando.skappserver.common.controller;

import cl.creando.skappserver.common.service.TenantService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/v1/tenants")
@AllArgsConstructor
public class TenantPublicController {

    private final TenantService tenantService;

    @GetMapping
    public ResponseEntity<?> list(@RequestParam(name = "searchTerm", required = false, defaultValue = "") String searchTerm, Pageable pageable) {
        return ResponseEntity.ok(tenantService.findAll(searchTerm, pageable));
    }
}
