package cl.creando.skappserver.workorder.repository;

import cl.creando.skappserver.workorder.entity.UserBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface UserBranchRepository extends JpaRepository<UserBranch, UUID>, JpaSpecificationExecutor<UserBranch> {
}
