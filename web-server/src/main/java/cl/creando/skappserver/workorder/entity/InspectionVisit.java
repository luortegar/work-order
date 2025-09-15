package cl.creando.skappserver.workorder.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

import static org.hibernate.annotations.UuidGenerator.Style.TIME;

@Entity
@Table(name = "tbl_inspection_visit")
@Getter
@Setter
public class InspectionVisit {
    @Id
    @GeneratedValue
    @UuidGenerator(style = TIME)
    @Column(name = "inspection_visit_id")
    private UUID inspectionVisitsId;

    private String title;
    private String descriptions;

    @JsonIgnore
    @OneToMany(mappedBy = "inspectionVisit", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<VisitPhoto> visitPhotoList;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "branchId")
    private Branch branch;
}
