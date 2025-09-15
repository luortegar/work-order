package cl.creando.skappserver.workorder.entity;

import cl.creando.skappserver.common.entity.common.AuditableEntity;
import cl.creando.skappserver.common.entity.common.File;
import cl.creando.skappserver.common.entity.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "tbl_work_order")
@Getter
@Setter
public class WorkOrder extends AuditableEntity {
    @Id
    @GeneratedValue
    @UuidGenerator(style = TIME)
    private UUID workOrderId;
    private String workOrderNumber;

    @Enumerated(EnumType.STRING)
    private WorkOrderStatus workOrderStatus;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "branchId")
    private Branch branch;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "recipientId")
    private User recipient;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "technicianId")
    private User technician;

    @JsonIgnore
    @OneToMany(mappedBy = "workOrder", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @Fetch(value = FetchMode.SUBSELECT)
    List<WorkOrderTechnician> workOrderTechnicianList;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "equipmentId")
    private Equipment equipment;

    private String serviceDetails;

    private String observations;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "signature_technician_file_id")
    private File signatureTechnician;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "signature_recipient_file_id")
    private File signatureRecipient;

    @JsonIgnore
    @OneToMany(mappedBy = "workOrder", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<WorkOrderPhoto> fileList;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @PrePersist
    public void prePersist() {
        if (workOrderStatus == null) {
            workOrderStatus = WorkOrderStatus.DRAFT;
        }
    }

}
