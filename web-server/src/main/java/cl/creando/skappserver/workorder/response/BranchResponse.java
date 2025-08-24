package cl.creando.skappserver.workorder.response;

import cl.creando.skappserver.workorder.entity.Branch;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class BranchResponse {

    private UUID branchId;
    private String branchName;
    private String address;
    private String commune;

    public BranchResponse(Branch branch) {
        this.branchId = branch.getBranchId();
        this.branchName = branch.getBranchName();
        this.address = branch.getAddress();
        this.commune = branch.getCommune();
    }
}
