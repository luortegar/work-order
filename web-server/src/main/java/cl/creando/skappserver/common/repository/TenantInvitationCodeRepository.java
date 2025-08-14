package cl.creando.skappserver.common.repository;

import cl.creando.skappserver.common.entity.common.EntityStatus;
import cl.creando.skappserver.common.entity.tenant.TenantInvitationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TenantInvitationCodeRepository extends JpaRepository<TenantInvitationCode, UUID>, JpaSpecificationExecutor<TenantInvitationCode> {
    Optional<TenantInvitationCode> findByCodeAndEntityStatus(String code, EntityStatus entityStatus );
}
