package cl.creando.skappserver.workorder.request;

import cl.creando.skappserver.workorder.entity.Equipment;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class EquipmentRequest {
    private String equipmentModel;
    private String equipmentBrand;
    private String serialNumber;
    private UUID equipmentTypeId;
    private UUID branchId;

    public void setValues(Equipment equipment) {
        equipment.setEquipmentModel(this.equipmentModel);
        equipment.setEquipmentBrand(this.equipmentBrand);
        equipment.setSerialNumber(this.serialNumber);
    }
}
