package cl.creando.skappserver.workorder.entity;

import cl.creando.skappserver.common.entity.common.AuditableEntity;
import cl.creando.skappserver.common.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

import static org.hibernate.annotations.UuidGenerator.Style.TIME;

@Entity
@Table(name = "tbl_user_client")
@Getter
@Setter
public class UserClient  extends AuditableEntity {
    @Id
    @GeneratedValue
    @UuidGenerator(style = TIME)
    @Column(name = "user_client_id")
    private UUID userClientId;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "clientId")
    private Client client;
}
