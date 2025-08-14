package cl.creando.skappserver.workorder.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class WorkOrderRequest {
    private String workOrderNumber;
    private UUID branchId;
    private UUID recipientId;
    private UUID technicianId;
    private UUID equipmentId;
    private String serviceDetails;
    private String observations;
    private List<UUID> photoIdList;
    private List<UUID> technicianIdList;
    private List<UUID> equipmentIdList;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
