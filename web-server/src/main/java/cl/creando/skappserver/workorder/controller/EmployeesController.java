package cl.creando.skappserver.workorder.controller;

import cl.creando.skappserver.common.request.UserRequest;
import cl.creando.skappserver.workorder.request.EmployeesRequest;
import cl.creando.skappserver.workorder.service.UserOtService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/private/v1/employees")
@AllArgsConstructor
public class EmployeesController {
    private final UserOtService userService;

    @GetMapping("/clients/{clientId}")
    public ResponseEntity<?> list(@PathVariable(name = "clientId") UUID clientId, @RequestParam(name = "searchTerm", required = false, defaultValue = "") String searchTerm, Pageable pageable) {
        return ResponseEntity.ok(userService.findAllByClientId(clientId, searchTerm, pageable));
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<?> userBranchList(@PathVariable(name = "branchId") UUID branchId, @RequestParam(name = "searchTerm", required = false, defaultValue = "") String searchTerm, Pageable pageable) {
        return ResponseEntity.ok(userService.findAllByBranchId(branchId, searchTerm, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> view(@PathVariable(name = "id") UUID id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody EmployeesRequest request) {
        return new ResponseEntity<>(userService.addEmployees(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") UUID id, @RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") UUID id) {
        return ResponseEntity.ok(userService.delete(id));
    }

    @GetMapping("/branch/{branchId}/autocomplete")
    public ResponseEntity<?> autocomplete(@PathVariable(name = "branchId") UUID branchId, @RequestParam(name = "searchTerm", required = false, defaultValue = "") String searchTerm) {
        return ResponseEntity.ok(userService.autocomplete(branchId, searchTerm));
    }
}
