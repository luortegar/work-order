package cl.creando.skappserver.common.controller;

import cl.creando.skappserver.common.request.RoleRequest;
import cl.creando.skappserver.common.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/private/v1/roles")
@AllArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<?> listRole(@RequestParam(name = "searchTerm", required = false, defaultValue = "") String searchTerm, Pageable pageable) {
        return roleService.findAll(searchTerm, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> viewRole(@PathVariable(name = "id") UUID id) {
        return roleService.findByRoleId(id);
    }

    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody RoleRequest request) {
        return roleService.add(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editRole(@PathVariable(name = "id") UUID id, @RequestBody RoleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.edit(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable(name = "id") UUID id) {
        return roleService.delete(id);
    }

}
