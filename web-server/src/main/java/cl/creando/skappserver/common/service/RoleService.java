package cl.creando.skappserver.common.service;


import cl.creando.skappserver.common.CommonFunctions;
import cl.creando.skappserver.common.exception.SKException;
import cl.creando.skappserver.common.entity.user.Privilege;
import cl.creando.skappserver.common.entity.user.Role;
import cl.creando.skappserver.common.entity.user.RolePrivilege;
import cl.creando.skappserver.common.repository.PrivilegeRepository;
import cl.creando.skappserver.common.repository.RolePrivilegeRepository;
import cl.creando.skappserver.common.repository.RoleRepository;
import cl.creando.skappserver.common.request.RoleRequest;
import cl.creando.skappserver.common.response.RoleAutocompleteResponse;
import cl.creando.skappserver.common.response.RoleDetailsResponse;
import cl.creando.skappserver.common.response.RoleResponse;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final RolePrivilegeRepository rolePrivilegeRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository, PrivilegeRepository privilegeRepository, RolePrivilegeRepository rolePrivilegeRepository) {
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.rolePrivilegeRepository = rolePrivilegeRepository;
    }

    public ResponseEntity<?> findAll(String searchTerm, Pageable pageable) {
        Specification<Role> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = List.of(
                    criteriaBuilder.like(root.get("roleId").as(String.class), CommonFunctions.getPattern(searchTerm)),
                    criteriaBuilder.like(root.get("roleName"), CommonFunctions.getPattern(searchTerm))
            );
            //noinspection ToArrayCallWithZeroLengthArrayArgument
            return criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<Role> all = roleRepository.findAll(specification, pageable);
        List<RoleResponse> list = all.map(RoleResponse::new).stream().toList();
        //noinspection unchecked,rawtypes
        Page page = new PageImpl(list, all.getPageable(), all.getTotalElements());
        return ResponseEntity.ok(page);
    }

    @Transactional
    public ResponseEntity<?> add(RoleRequest roleRequest) {
        List<UUID> uuids = roleRequest.getPrivilegeIds().stream().map(UUID::fromString).toList();
        List<Privilege> privileges = privilegeRepository.findAllById(uuids);
        Role role = new Role();
        role.setRoleName(roleRequest.getRoleName());
        Role roleSaved = roleRepository.save(role);
        List<RolePrivilege> rolePrivileges = privileges.stream().map(p -> {
            RolePrivilege rolePrivilege = new RolePrivilege();
            rolePrivilege.setPrivilege(p);
            rolePrivilege.setRole(roleSaved);
            return rolePrivilege;
        }).toList();
        rolePrivilegeRepository.saveAll(rolePrivileges);
        RoleDetailsResponse response = new RoleDetailsResponse(role, privileges);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Transactional
    public RoleDetailsResponse edit(UUID id, RoleRequest roleRequest) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new SKException("Invalid role id.", HttpStatus.BAD_REQUEST));
        role.setRoleName(roleRequest.getRoleName());
        rolePrivilegeRepository.deleteAll(rolePrivilegeRepository.findAllByRole(role));
        List<UUID> uuids = roleRequest.getPrivilegeIds().stream().map(UUID::fromString).toList();
        List<Privilege> privileges = privilegeRepository.findAllById(uuids);
        role.setRoleName(roleRequest.getRoleName());
        Role roleSaved = roleRepository.save(role);
        List<RolePrivilege> rolePrivileges = privileges.stream().map(p -> {
            RolePrivilege rolePrivilege = new RolePrivilege();
            rolePrivilege.setPrivilege(p);
            rolePrivilege.setRole(roleSaved);
            return rolePrivilege;
        }).toList();
        rolePrivilegeRepository.saveAll(rolePrivileges);
        return new RoleDetailsResponse(role, privileges);
    }

    public ResponseEntity<?> findByRoleId(UUID id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new SKException("Invalid role id.", HttpStatus.NOT_FOUND));

        List<Privilege> privileges = rolePrivilegeRepository.findAllByRole(role).stream().map(RolePrivilege::getPrivilege).toList();

        RoleDetailsResponse response = new RoleDetailsResponse(role, privileges);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<?> delete(UUID id) {
        try {
            Role role = roleRepository.findById(id)
                    .orElseThrow(() -> new SKException("Invalid role id.", HttpStatus.NOT_FOUND));
            roleRepository.delete(role);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (DataIntegrityViolationException e) {
            throw new SKException("Cannot delete role. It is referenced by other records.", HttpStatus.CONFLICT);

        } catch (Exception e) {
            throw new SKException("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> autocomplete(String searchTerm) {
        Specification<Role> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = List.of(
                    criteriaBuilder.like(root.get("roleId").as(String.class), CommonFunctions.getPattern(searchTerm)),
                    criteriaBuilder.like(root.get("roleName"), CommonFunctions.getPattern(searchTerm))
            );
            return criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]));
        };
        Pageable pageable = PageRequest.of(0, 10);
        Page<Role> all = roleRepository.findAll(specification, pageable);
        List<RoleAutocompleteResponse> list = all.map(RoleAutocompleteResponse::new).stream().toList();
        return ResponseEntity.ok(list);
    }
}
