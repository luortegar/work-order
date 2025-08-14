package cl.creando.skappserver.common.response;

import cl.creando.skappserver.common.entity.user.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
public class RoleResponse {
    private UUID roleId;
    private String roleName;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;

    public RoleResponse(Role role) {
        this.roleId = role.getRoleId();
        this.roleName = role.getRoleName();
        this.creationDate = role.getCreationDate();
        this.updateDate = role.getUpdateDate();
    }
}
