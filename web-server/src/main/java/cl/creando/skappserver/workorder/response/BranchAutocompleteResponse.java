package cl.creando.skappserver.workorder.response;

import cl.creando.skappserver.workorder.entity.Branch;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class BranchAutocompleteResponse {

    private UUID branchId;
    private String branchFullName;


    public BranchAutocompleteResponse(Branch branch) {
        this.branchId = branch.getBranchId();
        this.branchFullName = String.format("%s - %s (%s)",branch.getClient().getCompanyName(), branch.getBranchName(), branch.getAddress());
    }
}
