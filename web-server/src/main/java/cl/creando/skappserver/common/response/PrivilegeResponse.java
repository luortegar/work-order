package cl.creando.skappserver.common.response;

import cl.creando.skappserver.common.entity.user.Privilege;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PrivilegeResponse {
    private UUID privilegeId;
    private String privilegeName;
    private String privilegeSystemName;

    public PrivilegeResponse(Privilege privilege) {
        this.privilegeId = privilege.getPrivilegeId();
        this.privilegeName = privilege.getPrivilegeName();
        this.privilegeSystemName = privilege.getPrivilegeSystemName();
    }
}
