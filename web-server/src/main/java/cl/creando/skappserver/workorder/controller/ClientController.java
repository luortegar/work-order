package cl.creando.skappserver.workorder.controller;

import cl.creando.skappserver.workorder.request.ClientRequest;
import cl.creando.skappserver.workorder.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/private/v1/clients")
@AllArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public ResponseEntity<?> list(@RequestParam(name = "searchTerm", required = false, defaultValue = "") String searchTerm, Pageable pageable) {
        return ResponseEntity.ok(clientService.findAll(searchTerm, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> view(@PathVariable(name = "id") UUID id) {
        return ResponseEntity.ok(clientService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ClientRequest request) {
        return new ResponseEntity<>(clientService.add(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") UUID id, @RequestBody ClientRequest request) {
        return ResponseEntity.ok(clientService.edit(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") UUID roleId) {
        return ResponseEntity.ok(clientService.delete(roleId));
    }
}
