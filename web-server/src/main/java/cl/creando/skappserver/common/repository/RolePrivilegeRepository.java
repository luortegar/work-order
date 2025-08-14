package cl.creando.skappserver.common.repository;

import cl.creando.skappserver.common.entity.user.Role;
import cl.creando.skappserver.common.entity.user.RolePrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface RolePrivilegeRepository extends JpaRepository<RolePrivilege, UUID>, JpaSpecificationExecutor<RolePrivilege> {
    List<RolePrivilege> findAllByRole(Role role);
}
