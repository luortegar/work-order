package cl.creando.skappserver.workorder.repository;


import cl.creando.skappserver.workorder.entity.VisitPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface VisitPhotoRepository extends JpaRepository<VisitPhoto, UUID>, JpaSpecificationExecutor<VisitPhoto> {

}
