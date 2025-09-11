package cl.creando.skappserver.workorder.entity;

import cl.creando.skappserver.common.entity.common.AuditableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

import static org.hibernate.annotations.UuidGenerator.Style.TIME;

@Entity
@Table(name = "tbl_spare_part")
@Getter
@Setter
public class SparePart  extends AuditableEntity {
    @Id
    @GeneratedValue
    @UuidGenerator(style = TIME)
    private UUID sparePartId;
    private String partNumber;
    private String partDescription;
}
