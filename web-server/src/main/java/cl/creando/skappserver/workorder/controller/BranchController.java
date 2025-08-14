package cl.creando.skappserver.workorder.controller;

import cl.creando.skappserver.workorder.request.BranchRequest;
import cl.creando.skappserver.workorder.service.BranchService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/private/v1/branch-offices")
@AllArgsConstructor
public class BranchController {
    private final BranchService branchService;

    @GetMapping("/clients/{clientId}")
    public ResponseEntity<?> list(@PathVariable(name = "clientId") UUID clientId, @RequestParam(name = "searchTerm", required = false, defaultValue = "") String searchTerm, Pageable pageable) {
        return ResponseEntity.ok(branchService.findAll(clientId, searchTerm, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> view(@PathVariable(name = "id") UUID id) {
        return ResponseEntity.ok(branchService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody BranchRequest request) {
        return new ResponseEntity<>(branchService.add(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") UUID id, @RequestBody BranchRequest request) {
        return ResponseEntity.ok(branchService.edit(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") UUID id) {
        return ResponseEntity.ok(branchService.delete(id));
    }
}
