package cl.creando.skappserver.workorder.repository;

import cl.creando.skappserver.workorder.entity.WorkOrderPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface WorkOrderPhotoRepository extends JpaRepository<WorkOrderPhoto, UUID> {

}
