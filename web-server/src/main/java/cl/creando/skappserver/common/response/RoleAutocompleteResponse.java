package cl.creando.skappserver.common.response;

import cl.creando.skappserver.common.entity.user.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RoleAutocompleteResponse {

    private UUID roleId;
    private String roleFullName;

    public RoleAutocompleteResponse(Role role) {
        this.roleId = role.getRoleId();
        this.roleFullName = role.getRoleName();
    }
}
