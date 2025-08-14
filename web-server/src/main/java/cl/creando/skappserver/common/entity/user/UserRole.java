package cl.creando.skappserver.common.entity.user;


import cl.creando.skappserver.common.entity.common.AuditableEntity;
import cl.creando.skappserver.common.entity.common.EntityStatus;
import cl.creando.skappserver.common.entity.tenant.Tenant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

import static org.hibernate.annotations.UuidGenerator.Style.TIME;

@Entity
@Getter
@Setter
@Table(name = "tbl_user_role",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"userId", "tenantId"})
        })
public class UserRole extends AuditableEntity {

    @Id
    @GeneratedValue
    @UuidGenerator(style = TIME)
    private UUID userRoleId;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "roleId")
    private Role role;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "tenantId")
    private Tenant tenant;

    @Enumerated(EnumType.STRING)
    private EntityStatus entityStatus;

    private Boolean isDefault;

    @PrePersist
    public void prePersist() {
        this.entityStatus = EntityStatus.ACTIVE;
    }

}
