package cl.creando.skappserver.common.entity.address;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

import static org.hibernate.annotations.UuidGenerator.Style.TIME;

@Entity
@Table(name = "tbl_region")
@Getter
@Setter
public class Region {
    @Id
    @GeneratedValue
    @UuidGenerator(style = TIME)
    private UUID regionId;
    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "countryId", nullable = false)
    private Country country;
    @OneToMany(mappedBy = "region", fetch = FetchType.LAZY)
    private List<Commune> cities;
}