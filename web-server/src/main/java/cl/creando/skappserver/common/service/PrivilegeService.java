package cl.creando.skappserver.common.service;

import cl.creando.skappserver.common.repository.PrivilegeRepository;
import cl.creando.skappserver.common.response.PrivilegeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PrivilegeService {

    private final PrivilegeRepository privilegeRepository;

    @Autowired
    public PrivilegeService(PrivilegeRepository privilegeRepository) {
        this.privilegeRepository = privilegeRepository;
    }

    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(privilegeRepository.findAll().stream().map(PrivilegeResponse::new).toList());
    }
}
