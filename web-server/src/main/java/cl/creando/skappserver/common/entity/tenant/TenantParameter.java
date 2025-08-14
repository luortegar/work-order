package cl.creando.skappserver.common.entity.tenant;

import cl.creando.skappserver.common.entity.common.AuditableEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

import static org.hibernate.annotations.UuidGenerator.Style.TIME;

@Entity
@Table(name = "tbl_tenant_parameter")

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantParameter extends AuditableEntity {
    @Id
    @GeneratedValue
    @UuidGenerator(style = TIME)
    private UUID tenantParameterId;
    private String parameterName;
    private String parameterValue;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenantId")
    private Tenant tenant;
}
