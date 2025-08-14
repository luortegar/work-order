package cl.creando.skappserver.common.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UserTenant {
    private String tenantName;
    private String tenantId;
    private Boolean isDefault;
    private List<TenantPrivilege> tenantPrivilegeList;
}
