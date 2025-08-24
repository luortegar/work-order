package cl.creando.skappserver.workorder.controller;

import cl.creando.skappserver.workorder.request.EquipmentRequest;
import cl.creando.skappserver.workorder.service.EquipmentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/private/v1/equipments")
@AllArgsConstructor
public class EquipmentController {
    private final EquipmentService equipmentService;

    @GetMapping("/branch-offices/{branchId}")
    public ResponseEntity<?> list(@PathVariable(name = "branchId") UUID branchId, @RequestParam(name = "searchTerm", required = false, defaultValue = "") String searchTerm, Pageable pageable) {
        return ResponseEntity.ok(equipmentService.findAll(branchId, searchTerm, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> view(@PathVariable(name = "id") UUID id) {
        return ResponseEntity.ok(equipmentService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody EquipmentRequest request) {
        return new ResponseEntity<>(equipmentService.add(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") UUID id, @RequestBody EquipmentRequest request) {
        return ResponseEntity.ok(equipmentService.edit(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") UUID roleId) {
        return ResponseEntity.ok(equipmentService.delete(roleId));
    }

    @GetMapping("equipment-type/autocomplete")
    public ResponseEntity<?> autoCompleteEquipmentType(@RequestParam(name = "searchTerm", required = false, defaultValue = "") String searchTerm) {
        return ResponseEntity.ok(equipmentService.autoCompleteEquipmentType(searchTerm));
    }

    @GetMapping("/branch-offices/{branchId}/autoComplete")
    public ResponseEntity<?> autoCompleteEquipment(@PathVariable(name = "branchId") UUID branchId, @RequestParam(name = "searchTerm", required = false, defaultValue = "") String searchTerm) {
        return ResponseEntity.ok(equipmentService.autoCompleteEquipment(branchId, searchTerm));
    }
}
