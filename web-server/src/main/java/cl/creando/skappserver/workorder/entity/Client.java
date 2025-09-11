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
@Table(name = "tbl_client")
@Getter
@Setter
public class Client  extends AuditableEntity {
    @Id
    @GeneratedValue
    @UuidGenerator(style = TIME)
    private UUID clientId;

    private String companyName;
    private String uniqueTaxpayerIdentification;
    private String business;
    private String address;
    private String commune;
    private String city;
    @Enumerated(EnumType.STRING)
    private TypeOfPurchase typeOfPurchase;

    @JsonIgnore
    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Branch> branchList;

    @JsonIgnore
    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<UserClient> userClientList;

}
