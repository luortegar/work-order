package cl.creando.skappserver.workorder.entity;

import cl.creando.skappserver.common.entity.common.AuditableEntity;
import cl.creando.skappserver.common.entity.common.File;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

import static org.hibernate.annotations.UuidGenerator.Style.TIME;

@Entity
@Table(name = "tbl_work_order_photo")
@Getter
@Setter
public class WorkOrderPhoto  extends AuditableEntity {
    @Id
    @GeneratedValue
    @UuidGenerator(style = TIME)
    private UUID workOrderPhotoId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "photo_file_id")
    private File file;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "workOrderId")
    private WorkOrder workOrder;
}
