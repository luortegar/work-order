package cl.creando.skappserver.workorder.response;

import cl.creando.skappserver.workorder.entity.EquipmentType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class EquipmentTypeResponse {
    private UUID equipmentTypeId;
    private String typeName;

    public EquipmentTypeResponse(EquipmentType equipmentType){
        this.equipmentTypeId = equipmentType.getEquipmentTypeId();
        this.typeName = equipmentType.getTypeName();
    }
}
