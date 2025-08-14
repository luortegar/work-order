package cl.creando.skappserver.common.controller;

import cl.creando.skappserver.common.request.TenantParameterRequest;
import cl.creando.skappserver.common.request.TenantRequest;
import cl.creando.skappserver.common.service.TenantService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/private/v1/tenants")
@AllArgsConstructor
@SecurityRequirement(name = "Authorization")
public class TenantController {

    private final TenantService tenantService;

    @GetMapping
    public ResponseEntity<?> list(@RequestParam(name = "searchTerm", required = false, defaultValue = "") String searchTerm, Pageable pageable) {
        return ResponseEntity.ok(tenantService.findAll(searchTerm, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> view(@PathVariable(name = "id") UUID id) {
        return ResponseEntity.ok(tenantService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody TenantRequest tenantRequest) {
        return new ResponseEntity<>(tenantService.add(tenantRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") String id, @RequestBody TenantRequest tenantRequest) {
        return ResponseEntity.ok(tenantService.update(id, tenantRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") UUID id) {
        return ResponseEntity.ok(tenantService.delete(id));
    }

    @GetMapping("/{id}/parameters")
    public ResponseEntity<?> findAllTenantParametersByTenantId(@PathVariable(name = "id") UUID id) {
        return ResponseEntity.ok(tenantService.findAllTenantParametersByTenantId(id));
    }

    @PostMapping("/{id}/parameters")
    public ResponseEntity<?> addTenantParameter(@PathVariable(name = "id") UUID id, @RequestBody TenantParameterRequest tenantParameterRequest) {
        return new ResponseEntity<>(tenantService.addTenantParameter(id, tenantParameterRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/parameters/{tenantParameterId}")
    public ResponseEntity<?> updateTenantParameter(@PathVariable(name = "id") UUID id, @PathVariable(name = "tenantParameterId") String tenantParameterId, @RequestBody TenantParameterRequest tenantParameterRequest) {
        return ResponseEntity.ok(tenantService.updateTenantParameter(id, tenantParameterId, tenantParameterRequest));
    }

    @DeleteMapping("/{id}/parameters/{tenantParameterId}")
    public ResponseEntity<?> deleteTenantParameter(@PathVariable(name = "id") UUID id, @PathVariable(name = "tenantParameterId") String tenantParameterId) {
        return ResponseEntity.ok(tenantService.deleteTenantParameter(id, tenantParameterId));
    }
}
