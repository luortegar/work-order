package cl.creando.skappserver.workorder.entity;

import cl.creando.skappserver.common.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

import static org.hibernate.annotations.UuidGenerator.Style.TIME;

@Entity
@Table(name = "tbl_work_order_technician")
@Getter
@Setter
public class WorkOrderTechnician {
    @Id
    @GeneratedValue
    @UuidGenerator(style = TIME)
    private UUID workOrderTechnicianId;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "workOrderId")
    private WorkOrder workOrder;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "technicianId")
    private User technician;
}
