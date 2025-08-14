package cl.creando.skappserver.common.entity.user;

import cl.creando.skappserver.common.entity.common.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

import static org.hibernate.annotations.UuidGenerator.Style.TIME;

@Entity
@Table(name = "tbl_role_privilege")
@Getter
@Setter
public class RolePrivilege extends AuditableEntity {

    @Id
    @GeneratedValue
    @UuidGenerator(style = TIME)
    private UUID rolePrivilegeId;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId")
    private Role role;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "privilegeId")
    private Privilege privilege;
}
