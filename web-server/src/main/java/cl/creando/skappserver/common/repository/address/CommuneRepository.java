package cl.creando.skappserver.common.repository.address;

import cl.creando.skappserver.common.entity.address.Commune;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface CommuneRepository extends JpaRepository<Commune, UUID> {
    List<Commune> findAllByNameContaining(String searchTerm);

    List<Commune> findTop10ByNameContaining(String searchTerm);

    List<Commune> findTop10ByNameContainingIgnoreCase(String searchTerm);
}
