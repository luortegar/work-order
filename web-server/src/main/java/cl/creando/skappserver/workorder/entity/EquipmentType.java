package cl.creando.skappserver.workorder.entity;

import cl.creando.skappserver.common.entity.common.AuditableEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

import static org.hibernate.annotations.UuidGenerator.Style.TIME;

@Entity
@Table(name = "tbl_equipment_type")
@Getter
@Setter
public class EquipmentType  extends AuditableEntity {
    @Id
    @GeneratedValue
    @UuidGenerator(style = TIME)
    private UUID equipmentTypeId;
    private String typeName;

    @JsonIgnore
    @OneToMany(mappedBy = "equipmentType", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Equipment> equipmentList;
}
