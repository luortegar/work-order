package cl.creando.skappserver.common.service;

import cl.creando.skappserver.common.CommonFunctions;
import cl.creando.skappserver.common.entity.common.EntityStatus;
import cl.creando.skappserver.common.entity.tenant.Tenant;
import cl.creando.skappserver.common.entity.tenant.TenantParameter;
import cl.creando.skappserver.common.repository.TenantParameterRepository;
import cl.creando.skappserver.common.repository.TenantRepository;
import cl.creando.skappserver.common.request.TenantParameterRequest;
import cl.creando.skappserver.common.request.TenantRequest;
import cl.creando.skappserver.common.response.TenantParameterResponse;
import cl.creando.skappserver.common.response.TenantResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.module.ResolutionException;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;
    private final TenantParameterRepository tenantParameterRepository;

    public Page<TenantResponse> findAll(String searchTerm, Pageable pageable) {
        Specification<Tenant> specification = (root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(root.get("tenantName"), CommonFunctions.getPattern(searchTerm)));
        Page<Tenant> all = tenantRepository.findAll(specification, pageable);
        List<TenantResponse> list = all.map(TenantResponse::new).stream().toList();
        return new PageImpl<>(list, all.getPageable(), all.getTotalElements());
    }

    public TenantResponse findById(UUID id) {
        Tenant tenant = tenantRepository.findById(id).orElseThrow(RuntimeException::new);
        return new TenantResponse(tenant);
    }

    public TenantResponse add(TenantRequest tenantRequest) {
        Tenant tenant = Tenant.builder().tenantName(tenantRequest.getTenantName()).entityStatus(EntityStatus.ACTIVE).build();
        final Tenant tenantCreated = tenantRepository.save(tenant);
        return new TenantResponse(tenantCreated);
    }

    public TenantResponse update(String uuidString, TenantRequest tenantRequest) {
        UUID uuid = UUID.fromString(uuidString);
        final Tenant tenant = tenantRepository.findById(uuid).orElseThrow(RuntimeException::new);

        if (StringUtils.hasText(tenantRequest.getTenantName())) {
            tenant.setTenantName(tenantRequest.getTenantName());
        }
        if (StringUtils.hasText(tenantRequest.getEntityStatus())) {
            EntityStatus entityStatus = EntityStatus.getByKeyOrValue(tenantRequest.getEntityStatus()).orElseThrow(ResolutionException::new);
            tenant.setEntityStatus(entityStatus);
        }
        return new TenantResponse(tenantRepository.save(tenant));
    }

    public TenantResponse delete(UUID id) {
        Tenant tenant = tenantRepository.findById(id).orElseThrow(RuntimeException::new);
        TenantResponse tenantResponse = new TenantResponse(tenant);
        tenantRepository.delete(tenant);
        return tenantResponse;
    }

    public List<TenantParameter> findAllTenantParametersByTenantId(UUID id) {
        Tenant tenant = tenantRepository.findById(id).orElseThrow(RuntimeException::new);
        return tenantParameterRepository.findAllByTenant(tenant);
    }

    public TenantParameterResponse addTenantParameter(UUID id, TenantParameterRequest tenantRequest) {
        Tenant tenant = tenantRepository.findById(id).orElseThrow(RuntimeException::new);
        TenantParameter tenantParameter = TenantParameter.builder()
                .parameterName(tenantRequest.getParameterName())
                .parameterValue(tenantRequest.getParameterValue())
                .tenant(tenant).build();
        TenantParameter tenantParameterCreated = tenantParameterRepository.save(tenantParameter);

        return new TenantParameterResponse(tenantParameterCreated);
    }

    public TenantParameterResponse updateTenantParameter(UUID id, String tenantParameterId, TenantParameterRequest tenantParameterRequest) {
        tenantRepository.findById(id)
                .orElseThrow(RuntimeException::new);
        TenantParameter tenantParameter = tenantParameterRepository.findById(UUID.fromString(tenantParameterId))
                .orElseThrow(RuntimeException::new);

        tenantParameter.setParameterValue(tenantParameterRequest.getParameterValue());

        TenantParameter tenantParameterUpdated = tenantParameterRepository.save(tenantParameter);
        return new TenantParameterResponse(tenantParameterUpdated);
    }

    public TenantParameterResponse deleteTenantParameter(UUID id, String tenantParameterId) {
        tenantRepository.findById(id)
                .orElseThrow(RuntimeException::new);
        TenantParameter tenantParameter = tenantParameterRepository.findById(UUID.fromString(tenantParameterId))
                .orElseThrow(RuntimeException::new);
        tenantParameterRepository.delete(tenantParameter);
        return new TenantParameterResponse(tenantParameter);
    }
}
