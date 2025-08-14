package cl.creando.skappserver.workorder.repository;

import cl.creando.skappserver.workorder.entity.UserClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface UserClientRepository extends JpaRepository<UserClient, UUID>, JpaSpecificationExecutor<UserClient> {
}
