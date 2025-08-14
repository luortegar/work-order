package cl.creando.skappserver.common.response;

import cl.creando.skappserver.common.entity.tenant.Tenant;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Data
@Getter
@Setter
public class TenantResponse {

    private final UUID tenantId;
    private final String tenantName;
    private final String entityStatus;

    public TenantResponse(Tenant tenant) {
        this.tenantId = tenant.getTenantId();
        this.tenantName = tenant.getTenantName();
        this.entityStatus = tenant.getEntityStatus().getValue();
    }
}
