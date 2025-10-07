package cl.creando.skappserver.workorder.response;

import cl.creando.skappserver.workorder.entity.InspectionVisit;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class InspectionVisitResponse {

    private UUID inspectionVisitId;
    private String title;
    private String descriptions;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    public InspectionVisitResponse (InspectionVisit inspectionVisit){
        this.inspectionVisitId = inspectionVisit.getInspectionVisitId();
        this.title = inspectionVisit.getTitle();
        this.descriptions = inspectionVisit.getDescriptions();
        this.date = inspectionVisit.getDate();
    }
}
