package cl.creando.skappserver.workorder.entity;

import cl.creando.skappserver.common.entity.common.AuditableEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hibernate.annotations.UuidGenerator.Style.TIME;

@Entity
@Table(name = "tbl_inspection_visit")
@Getter
@Setter
public class InspectionVisit extends AuditableEntity {
    @Id
    @GeneratedValue
    @UuidGenerator(style = TIME)
    @Column(name = "inspection_visit_id")
    private UUID inspectionVisitId;

    private String title;
    private String descriptions;
    private LocalDateTime date;

    @JsonIgnore
    @OneToMany(mappedBy = "inspectionVisit", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<VisitPhoto> visitPhotoList;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "branchId")
    private Branch branch;
}
