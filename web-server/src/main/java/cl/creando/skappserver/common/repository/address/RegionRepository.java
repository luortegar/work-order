package cl.creando.skappserver.common.repository.address;

import cl.creando.skappserver.common.entity.address.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface RegionRepository extends JpaRepository<Region, UUID> {
}
