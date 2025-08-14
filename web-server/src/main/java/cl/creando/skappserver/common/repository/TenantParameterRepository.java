package cl.creando.skappserver.common.repository;

import cl.creando.skappserver.common.entity.tenant.Tenant;
import cl.creando.skappserver.common.entity.tenant.TenantParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TenantParameterRepository extends JpaRepository<TenantParameter, UUID>, JpaSpecificationExecutor<TenantParameter> {
    List<TenantParameter> findAllByTenant(Tenant tenant);
}
