package cl.creando.skappserver.workorder.response;

import cl.creando.skappserver.workorder.entity.WorkOrder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Getter
@Setter
public class WorkOrderResponse {

    private String workOrderId;
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
        this.startTime = workOrder.getStartTime();
        this.endTime = workOrder.getEndTime();
        this.creationDate = workOrder.getCreationDate();
        this.updateDate = workOrder.getUpdateDate();
    }
}
