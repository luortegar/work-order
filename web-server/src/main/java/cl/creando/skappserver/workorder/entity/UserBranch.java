package cl.creando.skappserver.workorder.entity;

import cl.creando.skappserver.common.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

import static org.hibernate.annotations.UuidGenerator.Style.TIME;


@Entity
@Table(name = "tbl_user_branch")
@Getter
@Setter
public class UserBranch {
    @Id
    @GeneratedValue
    @UuidGenerator(style = TIME)
    @Column(name = "user_branch_id")
    private UUID userBranchId;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "branchId")
    private Branch branch;
}
