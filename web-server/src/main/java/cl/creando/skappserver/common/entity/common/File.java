package cl.creando.skappserver.common.entity.common;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

import static org.hibernate.annotations.UuidGenerator.Style.TIME;

@Entity
@Table(name = "tbl_file")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class File extends AuditableEntity {
    @Id
    @GeneratedValue
    @UuidGenerator(style = TIME)
    private UUID fileId;
    private String fileName;
    private String filePath;
    private String contentType;
}
