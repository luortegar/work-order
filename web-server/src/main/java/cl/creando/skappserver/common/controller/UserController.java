package cl.creando.skappserver.common.controller;

import cl.creando.skappserver.common.request.UpdatePasswordRequest;
import cl.creando.skappserver.common.request.UserRequest;
import cl.creando.skappserver.common.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/private/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/my-user")
    public ResponseEntity<?> viewMyUser(Principal principal) {
        return ResponseEntity.ok(userService.viewMyUser(principal));
    }

    @PatchMapping("/update-my-user-password")
    public ResponseEntity<?> updateMyUserPassword(Principal principal,  @RequestBody UpdatePasswordRequest updatePasswordRequest ) {
        return ResponseEntity.ok(userService.updateMyUserPassword(principal, updatePasswordRequest));
    }

    @GetMapping
    public ResponseEntity<?> listUser(@RequestParam(name = "searchTerm", required = false, defaultValue = "") String searchTerm, Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(searchTerm, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> viewUser(@PathVariable(name = "id") UUID id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.add(userRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable(name = "id") UUID id, @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.update(id, userRequest));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> userPartialUpdate(@PathVariable(name = "id") UUID id, @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(userService.partialUpdate(id, updates));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable(name = "id") UUID id) {
        return ResponseEntity.ok(userService.delete(id));
    }

}
