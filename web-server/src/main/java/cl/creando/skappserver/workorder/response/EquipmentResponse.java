package cl.creando.skappserver.workorder.response;

import cl.creando.skappserver.workorder.entity.Equipment;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class EquipmentResponse {
    private UUID equipmentId;
    private String equipmentModel;
    private String equipmentBrand;
    private String serialNumber;
    private String equipmentType;
    private UUID equipmentTypeId;
    private UUID branchId;

    public EquipmentResponse(Equipment equipment) {
        this.equipmentId = equipment.getEquipmentId();
        this.equipmentModel = equipment.getEquipmentModel();
        this.equipmentBrand = equipment.getEquipmentBrand();
        this.serialNumber = equipment.getSerialNumber();
        if (equipment.getEquipmentType() != null) {
            this.equipmentType = equipment.getEquipmentType().getTypeName();
            this.equipmentTypeId = equipment.getEquipmentType().getEquipmentTypeId();
        }
        if(equipment.getBranch()!= null){
            this.branchId = equipment.getBranch().getBranchId();
        }
    }
}
