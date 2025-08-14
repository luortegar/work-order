package cl.creando.skappserver.common.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TenantPrivilege {
    private String privilegeName;
    private String privilegeSystemName;
}
