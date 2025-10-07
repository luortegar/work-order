package cl.creando.skappserver.workorder.controller;

import cl.creando.skappserver.workorder.service.UserOtService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private/v1/technicians")
@AllArgsConstructor
public class TechnicianController {
    private final UserOtService userService;

    @GetMapping("/autocomplete")
    public ResponseEntity<?> technicianAutocomplete(@RequestParam(name = "searchTerm", required = false, defaultValue = "") String searchTerm) {
        return ResponseEntity.ok(userService.technicianAutocomplete(searchTerm));
    }
}
