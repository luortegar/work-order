package cl.creando.skappserver.workorder.request;

import cl.creando.skappserver.workorder.entity.Branch;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class BranchRequest {

    private String branchName;
    private String address;
    private String commune;
    private UUID clientId;

    public void setValues(Branch branch) {
        branch.setBranchName(this.branchName);
        branch.setAddress(this.address);
        branch.setCommune(this.commune);
    }
}
