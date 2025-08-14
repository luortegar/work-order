package cl.creando.skappserver.common.response;

import cl.creando.skappserver.common.entity.user.Privilege;
import cl.creando.skappserver.common.entity.user.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
public class RoleDetailsResponse {
    private UUID roleId;
    private String roleName;


    private List<PrivilegeResponse> privileges;

    public RoleDetailsResponse(Role role, List<Privilege> privileges) {
        this.roleId = role.getRoleId();
        this.roleName = role.getRoleName();
        this.privileges = privileges.stream().map(PrivilegeResponse::new).toList();
    }
}
