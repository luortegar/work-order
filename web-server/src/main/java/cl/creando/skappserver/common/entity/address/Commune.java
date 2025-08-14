package cl.creando.skappserver.common.entity.address;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

import static org.hibernate.annotations.UuidGenerator.Style.TIME;

@Entity
@Table(name = "tbl_commune")
@Getter
@Setter
public class Commune {
    @Id
    @GeneratedValue
    @UuidGenerator(style = TIME)
    private UUID communeId;
    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "regionId", nullable = false)
    private Region region;
}
