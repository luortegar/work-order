package cl.creando.skappserver.common.entity.address;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

import static org.hibernate.annotations.UuidGenerator.Style.TIME;

@Entity
@Table(name = "tbl_country")
@Getter
@Setter
public class Country {
    @Id
    @GeneratedValue
    @UuidGenerator(style = TIME)
    private UUID countryId;
    private String name;
    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
    private List<Region> regions;
}



