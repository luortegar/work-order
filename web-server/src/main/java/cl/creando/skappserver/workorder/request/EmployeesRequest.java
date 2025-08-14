package cl.creando.skappserver.workorder.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeesRequest {
    private String firstName;
    private String lastName;
    private String email;
    private UUID clientId;
    private UUID branchId;
}
