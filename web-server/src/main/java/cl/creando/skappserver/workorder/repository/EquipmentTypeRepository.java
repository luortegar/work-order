package cl.creando.skappserver.workorder.repository;

import cl.creando.skappserver.workorder.entity.EquipmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EquipmentTypeRepository extends JpaRepository<EquipmentType, UUID>, JpaSpecificationExecutor<EquipmentType> {
    List<EquipmentType> findTop10ByTypeNameContainingIgnoreCase(String searchTerm);
}
