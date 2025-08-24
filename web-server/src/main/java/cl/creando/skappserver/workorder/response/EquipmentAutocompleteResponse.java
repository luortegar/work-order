package cl.creando.skappserver.workorder.response;

import cl.creando.skappserver.workorder.entity.Equipment;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class EquipmentAutocompleteResponse {
    private UUID equipmentId;
    private String equipmentFullName;

    public EquipmentAutocompleteResponse(Equipment equipment){
        this.equipmentId = equipment.getEquipmentId();
        this.equipmentFullName = String
                .format("%s %s (%s)",
                        equipment.getEquipmentBrand(),
                        equipment.getEquipmentModel(),
                        equipment.getSerialNumber());
    }
}
