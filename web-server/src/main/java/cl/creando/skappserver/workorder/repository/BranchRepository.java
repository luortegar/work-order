package cl.creando.skappserver.workorder.repository;

import cl.creando.skappserver.workorder.entity.Branch;
import cl.creando.skappserver.workorder.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface BranchRepository extends JpaRepository<Branch, UUID>, JpaSpecificationExecutor<Branch> {

    Optional<Branch> findByClientAndBranchId(Client client, UUID id);
}
