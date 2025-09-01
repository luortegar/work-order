package cl.creando.skappserver.common.service;


import cl.creando.skappserver.common.email.EmailService;
import cl.creando.skappserver.common.entity.common.EntityStatus;
import cl.creando.skappserver.common.entity.tenant.TenantInvitationCode;
import cl.creando.skappserver.common.entity.user.User;
import cl.creando.skappserver.common.exception.SKException;
import cl.creando.skappserver.common.properties.StartedKitProperties;
import cl.creando.skappserver.common.repository.TenantInvitationCodeRepository;
import cl.creando.skappserver.common.repository.UserRepository;
import cl.creando.skappserver.common.request.*;
import cl.creando.skappserver.common.response.AuthenticationResponse;
import cl.creando.skappserver.common.response.RecoverPasswordResponse;
import cl.creando.skappserver.common.response.TenantPrivilege;
import cl.creando.skappserver.common.response.UserTenant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TenantInvitationCodeRepository tenantInvitationCodeRepository;
    private final EmailService emailService;
    private final StartedKitProperties startedKitProperties;

    private static List<UserTenant> getUserTenants(User user) {
        List<UserTenant> tenantDetailsList = new ArrayList<>();

        user.getUserRoleList().forEach(ur -> {
                    tenantDetailsList.add(UserTenant.builder().tenantName(ur.getTenant() != null ? ur.getTenant().getTenantName() : "global privileges")
                            .tenantId(ur.getTenant() != null ? ur.getTenant().getTenantId().toString() : null)
                            .isDefault(ur.getTenant() != null ? ur.getIsDefault() : false)
                            .tenantPrivilegeList(
                                    ur.getRole().getRolePrivilegeList().stream().map(
                                            rp -> TenantPrivilege.builder()
                                                    .privilegeName(rp.getPrivilege().getPrivilegeName())
                                                    .privilegeSystemName(rp.getPrivilege().getPrivilegeSystemName())
                                                    .build()
                                    ).collect(Collectors.toList())
                            )
                            .build());
                }
        );
        return tenantDetailsList;
    }

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        TenantInvitationCode tenantInvitationCode = tenantInvitationCodeRepository
                .findByCodeAndEntityStatus(request.getInvitationCode(), EntityStatus.ACTIVE)
                .orElseThrow(()->new SKException("Invalid invitation code.", HttpStatus.BAD_REQUEST));

        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    throw new SKException("The email is already register.", HttpStatus.BAD_REQUEST);
                });

        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .validationCode(UUID.randomUUID().toString())
                //.role(Role.USER)
                .build();
        emailService.sendSimpleMail(
                request.getEmail(),
                "Welcome to Easy Demo!",
                String.format("Url to validate account : %s/login?validationCode=%s", startedKitProperties.getBase().getUiUrl(), user.getValidationCode()));

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        List<UserTenant> tenantDetailsList = getUserTenants(user);

        var jwtToken = jwtService.generateToken(user);
        String jwtRefreshToken = jwtService.generateRefreshToken(user);


        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(jwtRefreshToken)
                .tenantDetailsList(tenantDetailsList)
                .build();
    }

    //todo: test
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
        Claims claims = jwtService.extractAllClaims(request.getRefreshToken());
        String userId = (String) claims.get("userId");
        var user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow();

        List<UserTenant> tenantDetailsList = getUserTenants(user);

        var jwtToken = jwtService.generateToken(user);
        String jwtRefreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(jwtRefreshToken)
                .tenantDetailsList(tenantDetailsList)
                .build();
    }

    public RecoverPasswordResponse recoverPassword(RecoverPasswordRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            return new RecoverPasswordResponse("If an account with that email address exists, you will receive instructions on how to reset your password.");
        }

        User user = userOptional.get();

        String validationToken = UUID.randomUUID().toString();
        user.setValidationCode(validationToken);
        userRepository.save(user);

        String recoveryUrl = String.format("%s/change-password?validationCode=%s", startedKitProperties.getBase().getUiUrl(), validationToken);
        String emailSubject = "Easy Demo Change Recovery";
        String emailBody = String.format("Please click the following link to reset your password: %s", recoveryUrl);

        emailService.sendSimpleMail(request.getEmail(), emailSubject, emailBody);

        return new RecoverPasswordResponse("If an account with that email address exists, you will receive instructions on how to reset your password.");
    }

    public AuthenticationResponse authenticateWithCode(AuthenticationWithCodeRequest request) {

        var user = userRepository.findByValidationCode(request.getValidationCode())
                .orElseThrow();

        List<UserTenant> tenantDetailsList = getUserTenants(user);

        var jwtToken = jwtService.generateToken(user);
        String jwtRefreshToken = jwtService.generateRefreshToken(user);

        user.setValidationCode(null);
        userRepository.save(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(jwtRefreshToken)
                .tenantDetailsList(tenantDetailsList)
                .build();
    }

    public AuthenticationResponse changePasswordAndLoginWithCode(ChangePasswordAndLoginWithCode request) {
        var user = userRepository.findByValidationCode(request.getValidationCode())
                .orElseThrow(()-> new SKException("Link to change expired password, go back to the option to change password.", HttpStatus.BAD_REQUEST));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        user.setValidationCode(null);
        userRepository.save(user);

        List<UserTenant> tenantDetailsList = getUserTenants(user);

        var jwtToken = jwtService.generateToken(user);
        String jwtRefreshToken = jwtService.generateRefreshToken(user);


        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(jwtRefreshToken)
                .tenantDetailsList(tenantDetailsList)
                .build();
    }
}
