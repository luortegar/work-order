package cl.creando.skappserver.common.entity.user;

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
@Table(name = "tbl_privilege")
@Getter
@Setter
public class Privilege {
    @Id
    @GeneratedValue
    @UuidGenerator(style = TIME)
    private UUID privilegeId;
    private String privilegeName;
    private String privilegeSystemName;
    @JsonIgnore
    @OneToMany(mappedBy = "privilege", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<RolePrivilege> rolePrivilegeList;
}
