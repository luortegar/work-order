package cl.creando.skappserver.common.response;

import cl.creando.skappserver.common.entity.tenant.TenantParameter;
import lombok.Data;

import java.util.UUID;


@Data
public class TenantParameterResponse {
    private final UUID tenantParameterId;
    private final String parameterName;
    private final String parameterValue;

    public TenantParameterResponse(TenantParameter tenantParameter) {
        this.tenantParameterId = tenantParameter.getTenantParameterId();
        this.parameterName = tenantParameter.getParameterName();
        this.parameterValue = tenantParameter.getParameterValue();
    }
}
