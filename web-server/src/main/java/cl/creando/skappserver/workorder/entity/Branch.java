package cl.creando.skappserver.workorder.entity;


import cl.creando.skappserver.common.entity.common.AuditableEntity;
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
@Table(name = "tbl_branch")
@Getter
@Setter
public class Branch  extends AuditableEntity {
    @Id
    @GeneratedValue
    @UuidGenerator(style = TIME)
    @Column(name = "id_branch")
    private UUID branchId;
    private String branchName;
    private String address;
    private String commune;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "clientId")
    private Client client;

    @JsonIgnore
    @OneToMany(mappedBy = "branch", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Equipment> equipmentList;

    @JsonIgnore
    @OneToMany(mappedBy = "branch", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<WorkOrder> workOrderList;

    @JsonIgnore
    @OneToMany(mappedBy = "branch", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<UserBranch> userBranchList;

    @JsonIgnore
    @OneToMany(mappedBy = "branch", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<InspectionVisit> inspectionVisitList;
}
