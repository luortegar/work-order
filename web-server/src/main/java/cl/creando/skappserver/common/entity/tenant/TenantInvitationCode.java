package cl.creando.skappserver.common.entity.tenant;

import cl.creando.skappserver.common.entity.common.AuditableEntity;
import cl.creando.skappserver.common.entity.common.EntityStatus;
import cl.creando.skappserver.common.entity.user.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

import static org.hibernate.annotations.UuidGenerator.Style.TIME;

@Entity
@Table(name = "tbl_tenant_invitation_code")

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantInvitationCode extends AuditableEntity {
    @Id
    @GeneratedValue
    @UuidGenerator(style = TIME)
    private UUID tenantInvitationCodeId;
    private String code;
    @Enumerated(EnumType.STRING)
    private EntityStatus entityStatus;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tenantId")
    private Tenant tenant;
}
