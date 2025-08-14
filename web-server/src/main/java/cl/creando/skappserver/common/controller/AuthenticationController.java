package cl.creando.skappserver.common.controller;

import cl.creando.skappserver.common.request.*;
import cl.creando.skappserver.common.response.AuthenticationResponse;
import cl.creando.skappserver.common.response.RecoverPasswordResponse;
import cl.creando.skappserver.common.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/v1/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/authenticate-with-code")
    public ResponseEntity<AuthenticationResponse> authenticateWithCode(@RequestBody AuthenticationWithCodeRequest request) {
        return ResponseEntity.ok(service.authenticateWithCode(request));
    }

    @PostMapping("/change-password-and-login-with-code")
    public ResponseEntity<AuthenticationResponse> changePasswordAndLoginWithCode(@RequestBody ChangePasswordAndLoginWithCode request) {
        return ResponseEntity.ok(service.changePasswordAndLoginWithCode(request));
    }

    @PostMapping("/recovery-password")
    public ResponseEntity<RecoverPasswordResponse> recoverPassword(@RequestBody RecoverPasswordRequest request) {
        return ResponseEntity.ok(service.recoverPassword(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(service.refreshToken(request));
    }
}
