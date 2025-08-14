package cl.creando.skappserver.workorder.response;

import cl.creando.skappserver.workorder.entity.WorkOrder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class WorkOrderResponse {

    private String workOrderId;
    private String workOrderNumber;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;

    public WorkOrderResponse(){

    }

    public WorkOrderResponse(WorkOrder workOrder) {
        this.workOrderId = workOrder.getWorkOrderId().toString();
        this.workOrderNumber = workOrder.getWorkOrderNumber();
        this.startTime = workOrder.getStartTime();
        this.endTime = workOrder.getEndTime();
        this.creationDate = workOrder.getCreationDate();
        this.updateDate = workOrder.getUpdateDate();
    }
}
