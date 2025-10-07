package cl.creando.skappserver.workorder.response;

import cl.creando.skappserver.workorder.entity.WorkOrder;
import cl.creando.skappserver.workorder.entity.WorkOrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class WorkOrderResponse {

    private String workOrderId;

    private WorkOrderStatus status;
    private String workOrderNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateDate;

    public WorkOrderResponse(){

    }

    public WorkOrderResponse(WorkOrder workOrder) {
        this.workOrderId = workOrder.getWorkOrderId().toString();
        this.workOrderNumber = workOrder.getWorkOrderNumber();
        this.status = workOrder.getWorkOrderStatus();
        this.startTime = workOrder.getStartTime();
        this.endTime = workOrder.getEndTime();
        this.creationDate = workOrder.getCreationDate();
        this.updateDate = workOrder.getUpdateDate();
    }
}
