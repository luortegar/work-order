package cl.creando.skappserver.workorder.response;

import cl.creando.skappserver.common.response.FileResponse;
import cl.creando.skappserver.workorder.entity.WorkOrder;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class DetailedWorkOrderResponse {
    private UUID workOrderId;
    private String workOrderNumber;
    private UUID branchId;
    private UUID recipientId;
    private UUID technicianId;
    private UUID equipmentId;
    private String serviceDetails;
    private String observations;
    private List<FileResponse> listOfPhotos;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    private String recipientSignatureBase64;
    private String technicianSignatureBase64;

    public DetailedWorkOrderResponse(WorkOrder workOrder) {
        this.workOrderId = workOrder.getWorkOrderId();
        this.workOrderNumber = workOrder.getWorkOrderNumber();
        if (workOrder.getBranch() != null) {
            this.branchId = workOrder.getBranch().getBranchId();
        }
        if (workOrder.getRecipient() != null) {
            this.recipientId = workOrder.getRecipient().getUserId();
        }
        if (workOrder.getTechnician() != null) {
            this.technicianId = workOrder.getTechnician().getUserId();
        }
        if (workOrder.getEquipment() != null) {
            this.equipmentId = workOrder.getEquipment().getEquipmentId();
        }
        this.serviceDetails = workOrder.getServiceDetails();
        this.observations = workOrder.getObservations();
        this.startTime = workOrder.getStartTime();
        this.endTime = workOrder.getEndTime();
    }
}
