package cl.creando.skappserver.workorder.service;


import cl.creando.skappserver.common.CommonFunctions;
import cl.creando.skappserver.common.entity.common.File;
import cl.creando.skappserver.common.entity.user.Role;
import cl.creando.skappserver.common.entity.user.User;
import cl.creando.skappserver.common.entity.user.UserRole;
import cl.creando.skappserver.common.exception.SKException;
import cl.creando.skappserver.common.properties.StartedKitProperties;
import cl.creando.skappserver.common.repository.FileRepository;
import cl.creando.skappserver.common.repository.RoleRepository;
import cl.creando.skappserver.common.repository.UserRepository;
import cl.creando.skappserver.common.repository.UserRoleRepository;
import cl.creando.skappserver.common.request.UpdatePasswordRequest;
import cl.creando.skappserver.common.request.UserRequest;
import cl.creando.skappserver.common.response.UserResponse;
import cl.creando.skappserver.workorder.entity.Branch;
import cl.creando.skappserver.workorder.entity.Client;
import cl.creando.skappserver.workorder.entity.UserBranch;
import cl.creando.skappserver.workorder.entity.UserClient;
import cl.creando.skappserver.workorder.repository.BranchRepository;
import cl.creando.skappserver.workorder.repository.ClientRepository;
import cl.creando.skappserver.workorder.repository.UserBranchRepository;
import cl.creando.skappserver.workorder.repository.UserClientRepository;
import cl.creando.skappserver.workorder.request.EmployeesRequest;
import cl.creando.skappserver.workorder.response.UserAutocompleteResponse;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserOtService {

    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final PasswordEncoder passwordEncoder;
    private final StartedKitProperties startedKitProperties;
    private final ClientRepository clientRepository;
    private final UserClientRepository userClientRepository;
    private final BranchRepository branchRepository;
    private final UserBranchRepository userBranchRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    public Page<?> findAll(String searchTerm, Pageable pageable) {
        Specification<User> specification = ((root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("updateDate")));
            return criteriaBuilder.or(
            criteriaBuilder.like(root.get("firstName"), CommonFunctions.getPattern(searchTerm)),
            criteriaBuilder.like(root.get("lastName"), CommonFunctions.getPattern(searchTerm)),
            criteriaBuilder.like(root.get("email"), CommonFunctions.getPattern(searchTerm))
    );
        });
        Page<User> all = userRepository.findAll(specification, pageable);
        List<UserResponse> list = all.map(UserResponse::new).stream().toList();
        return new PageImpl<>(list, all.getPageable(), all.getTotalElements());
    }

    public UserResponse findById(UUID id) {
        User user = userRepository.findById(id).orElseThrow();
        return new UserResponse(user, startedKitProperties.getBase().getApiUrl());
    }

    public UserResponse add(UserRequest userRequest) {
        User user = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .build();
        return new UserResponse(userRepository.save(user));
    }

    public UserResponse update(UUID uuid, UserRequest userRequest) {
        User user = userRepository.findById(uuid).orElseThrow();
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
        return new UserResponse(userRepository.save(user));
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

        if(!user.getPassword().equals(passwordEncoder.encode(updatePasswordRequest.getCurrentPassword()))){
            throw new SKException("Invalid old Password", HttpStatus.BAD_REQUEST);
        }
        user.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));

        return new UserResponse(user,  startedKitProperties.getBase().getApiUrl());
    }

    public Object findAllByClientId(UUID clientId, String searchTerm, Pageable pageable) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new SKException("Client not found.", HttpStatus.NOT_FOUND));

        Specification<User> specification = (root, query, criteriaBuilder) -> {
            Join<User, UserClient> userClientJoin = root.join("userClientList", JoinType.INNER);
            Predicate searchPredicate = criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), CommonFunctions.getPattern(searchTerm)),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), CommonFunctions.getPattern(searchTerm)),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), CommonFunctions.getPattern(searchTerm))
            );

            Predicate clientPredicate = criteriaBuilder.equal(userClientJoin.get("client"), client);

            return criteriaBuilder.and(searchPredicate, clientPredicate);
        };

        Page<User> all = userRepository.findAll(specification, pageable);
        List<UserResponse> list = all.map(UserResponse::new).stream().toList();
        return new PageImpl<>(list, all.getPageable(), all.getTotalElements());
    }

    @Transactional
    public Object addEmployees(EmployeesRequest request) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new SKException("Client not found.", HttpStatus.NOT_FOUND));


        userRepository.findByEmail(request.getEmail())
                .ifPresent(u -> {
                    throw new SKException(
                            String.format("There is already a user with this email (%s) address.",
                                    request.getEmail()),
                            HttpStatus.BAD_REQUEST
                    );
                });

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .build();
        user = userRepository.save(user);

        UserClient userClient = new UserClient();
        userClient.setClient(client);
        userClient.setUser(user);
        userClientRepository.save(userClient);

        Role role = roleRepository.findByRoleName("External user").orElseThrow(()->new SKException("Role not found.", HttpStatus.INTERNAL_SERVER_ERROR));

        UserRole userRole = new UserRole();
        userRole.setRole(role);
        userRole.setUser(user);
        userRoleRepository.save(userRole);

        if(request.getBranchId() != null){
            Branch branch = branchRepository.findById(request.getBranchId()) .orElseThrow(() -> new SKException("Branch not found.", HttpStatus.NOT_FOUND));
            UserBranch userBranch = new UserBranch();
            userBranch.setBranch(branch);
            userBranch.setUser(user);
            userBranchRepository.save(userBranch);
        }

        return new UserResponse(user);
    }

    public Object findAllByBranchId(UUID branchId, String searchTerm, Pageable pageable) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new SKException("Branch not found.", HttpStatus.NOT_FOUND));

        Specification<User> specification = (root, query, criteriaBuilder) -> {
            Join<User, UserBranch> userClientJoin = root.join("userBranchList", JoinType.INNER);
            Predicate searchPredicate = criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), CommonFunctions.getPattern(searchTerm)),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), CommonFunctions.getPattern(searchTerm)),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), CommonFunctions.getPattern(searchTerm))
            );

            Predicate clientPredicate = criteriaBuilder.equal(userClientJoin.get("branch"), branch);

            return criteriaBuilder.and(searchPredicate, clientPredicate);
        };

        Page<User> all = userRepository.findAll(specification, pageable);
        List<UserResponse> list = all.map(UserResponse::new).stream().toList();
        return new PageImpl<>(list, all.getPageable(), all.getTotalElements());
    }

    public List<UserAutocompleteResponse> autocomplete(UUID branchId, String searchTerm) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new SKException("Branch not found.", HttpStatus.NOT_FOUND));
        Client client = branch.getClient();

        Specification<User> specification = (root, query, cb) -> {
            query.distinct(true);

            Join<User, UserBranch> userBranchJoin = root.join("userBranchList", JoinType.LEFT);
            Join<User, UserClient> userClientJoin = root.join("userClientList", JoinType.LEFT);

            Predicate branchPredicate = cb.equal(userBranchJoin.get("branch"), branch);
            Predicate clientPredicate = cb.equal(userClientJoin.get("client"), client);

            Predicate searchPredicate = cb.or(
                    cb.like(cb.lower(root.get("firstName")), CommonFunctions.getPattern(searchTerm)),
                    cb.like(cb.lower(root.get("lastName")), CommonFunctions.getPattern(searchTerm)),
                    cb.like(cb.lower(root.get("email")), CommonFunctions.getPattern(searchTerm))
            );

            return cb.and(searchPredicate, cb.or(branchPredicate, clientPredicate));
        };

        Pageable pageable = PageRequest.of(0, 10); // 🔹 límite en la query
        Page<User> users = userRepository.findAll(specification, pageable);

        return users.stream()
                .map(UserAutocompleteResponse::new)
                .toList();
    }


    public List<UserAutocompleteResponse> technicianAutocomplete(String searchTerm) {
        Role role = roleRepository.findByRoleName("Technician")
                .orElseThrow(() -> new SKException("Role not found", HttpStatus.INTERNAL_SERVER_ERROR));

        Specification<User> specification = (root, query, criteriaBuilder) -> {
            Join<Object, Object> userRoleJoin = root.join("userRoleList", JoinType.INNER);
            Join<Object, Object> roleJoin = userRoleJoin.join("role", JoinType.INNER);
            Predicate searchPredicate = criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), CommonFunctions.getPattern(searchTerm)),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), CommonFunctions.getPattern(searchTerm)),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), CommonFunctions.getPattern(searchTerm))
            );
            Predicate rolePredicate = criteriaBuilder.equal(roleJoin.get("roleId"), role.getRoleId());
            return criteriaBuilder.and(searchPredicate, rolePredicate);
        };
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> all = userRepository.findAll(specification, pageable);
        return all.map(UserAutocompleteResponse::new).stream().toList();
    }


}
