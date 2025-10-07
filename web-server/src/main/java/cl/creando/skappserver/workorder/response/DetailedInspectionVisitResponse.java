package cl.creando.skappserver.workorder.response;

import cl.creando.skappserver.workorder.entity.InspectionVisit;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DetailedInspectionVisitResponse {

    private String title;
    private String descriptions;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;
    public DetailedInspectionVisitResponse(InspectionVisit inspectionVisit){
        this.title = inspectionVisit.getTitle();
        this.descriptions = inspectionVisit.getDescriptions();
        this.date = inspectionVisit.getDate();
    }
}
