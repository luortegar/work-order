package cl.creando.skappserver.common.service;


import cl.creando.skappserver.common.CommonFunctions;
import cl.creando.skappserver.common.entity.user.Role;
import cl.creando.skappserver.common.entity.user.UserRole;
import cl.creando.skappserver.common.exception.SKException;
import cl.creando.skappserver.common.entity.common.File;
import cl.creando.skappserver.common.entity.user.User;
import cl.creando.skappserver.common.properties.StartedKitProperties;
import cl.creando.skappserver.common.repository.RoleRepository;
import cl.creando.skappserver.common.repository.UserRoleRepository;
import cl.creando.skappserver.common.request.UpdatePasswordRequest;
import cl.creando.skappserver.common.repository.FileRepository;
import cl.creando.skappserver.common.repository.UserRepository;
import cl.creando.skappserver.common.request.UserRequest;
import cl.creando.skappserver.common.response.UserResponse;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final PasswordEncoder passwordEncoder;
    private final StartedKitProperties startedKitProperties;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;



    public Page<?> findAll(String searchTerm, Pageable pageable) {
        Specification<User> specification = (root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(root.get("firstName"), CommonFunctions.getPattern(searchTerm)),
                criteriaBuilder.like(root.get("lastName"), CommonFunctions.getPattern(searchTerm)),
                criteriaBuilder.like(root.get("email"), CommonFunctions.getPattern(searchTerm))
        );
        Page<User> all = userRepository.findAll(specification, pageable);
        List<UserResponse> list = all.map(UserResponse::new).stream().toList();
        return new PageImpl<>(list, all.getPageable(), all.getTotalElements());
    }

    public UserResponse findById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(()->new SKException("Invalid user id.", HttpStatus.BAD_REQUEST));
        return new UserResponse(user, startedKitProperties.getBase().getApiUrl());
    }

    public UserResponse add(UserRequest userRequest) {
        User user = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .build();

        if (userRequest.getRoleIds() != null && !userRequest.getRoleIds().isEmpty()) {
            for (UUID roleId : userRequest.getRoleIds()) {
                Role role = roleRepository.findById(roleId)
                        .orElseThrow(() -> new SKException("Invalid role", HttpStatus.BAD_REQUEST));

                UserRole userRole = new UserRole();
                userRole.setUser(user);   // importante para mantener la relación
                userRole.setRole(role);

                if(user.getUserRoleList() == null){
                    user.setUserRoleList(new ArrayList<>());
                }
                user.getUserRoleList().add(userRole);
            }
        }
        User userSaved = userRepository.save(user);

        return new UserResponse(userSaved);
    }


    public UserResponse update(UUID uuid, UserRequest userRequest) {
        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new SKException("Invalid user id.", HttpStatus.BAD_REQUEST));

        if (StringUtils.hasText(userRequest.getFirstName())) {
            user.setFirstName(userRequest.getFirstName());
        }
        if (StringUtils.hasText(userRequest.getLastName())) {
            user.setLastName(userRequest.getLastName());
        }
        if (StringUtils.hasText(userRequest.getEmail())) {
            user.setEmail(userRequest.getEmail());
        }
        if (StringUtils.hasText(userRequest.getPassword())) {
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }

        user.getUserRoleList().clear();

        if (userRequest.getRoleIds() != null && !userRequest.getRoleIds().isEmpty()) {
            for (UUID roleId : userRequest.getRoleIds()) {
                Role role = roleRepository.findById(roleId)
                        .orElseThrow(() -> new SKException("Invalid role", HttpStatus.BAD_REQUEST));

                UserRole userRole = new UserRole();
                userRole.setUser(user);
                userRole.setRole(role);

                user.getUserRoleList().add(userRole);
            }
        }

        User userSaved = userRepository.save(user);

        return new UserResponse(userSaved);
    }


    public ResponseEntity<?> delete(UUID uuid) {
        User user = userRepository.findById(uuid).orElseThrow();
        userRepository.delete(user);
        return ResponseEntity.ok(new UserResponse(user));
    }

    public UserResponse partialUpdate(UUID id, Map<String, Object> updates) {
        User user = userRepository.findById(id).orElseThrow((() -> new SKException("User not found.", HttpStatus.NOT_FOUND)));

        updates.forEach((key, value) -> {
            switch (key) {
                case "firstName":
                    user.setFirstName((String) value);
                    break;
                case "lastName":
                    user.setLastName((String) value);
                    break;
                case "profilePictureFileId":
                    File profilePictureFile = fileRepository.findById(UUID.fromString((String) value)).orElseThrow(() -> new SKException("File not found.", HttpStatus.NOT_FOUND));
                    user.setProfilePictureFileId(profilePictureFile);
                    break;
            }
        });
        return new UserResponse(userRepository.save(user), startedKitProperties.getBase().getApiUrl());
    }

    public UserResponse viewMyUser(Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow(()-> new SKException("Invalid user session.", HttpStatus.NOT_FOUND));
        return new UserResponse(user,  startedKitProperties.getBase().getApiUrl());
    }

    public Object updateMyUserPassword(Principal principal, UpdatePasswordRequest updatePasswordRequest) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow(()-> new SKException("Invalid user session.", HttpStatus.NOT_FOUND));

        if(! passwordEncoder.matches(updatePasswordRequest.getCurrentPassword(), user.getPassword())){
            throw new SKException("Invalid old Password", HttpStatus.BAD_REQUEST);
        }
        user.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
        userRepository.save(user);

        return new UserResponse(user,  startedKitProperties.getBase().getApiUrl());
    }




}
