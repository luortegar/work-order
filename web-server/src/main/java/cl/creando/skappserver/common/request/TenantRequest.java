package cl.creando.skappserver.common.request;

import lombok.Data;

@Data
public class TenantRequest {
    private String tenantName;
    private String entityStatus;
}
