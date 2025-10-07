package cl.creando.skappserver.workorder.repository;


import cl.creando.skappserver.workorder.entity.InspectionVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InspectionVisitRepository extends JpaRepository<InspectionVisit, UUID>, JpaSpecificationExecutor<InspectionVisit> {

}
