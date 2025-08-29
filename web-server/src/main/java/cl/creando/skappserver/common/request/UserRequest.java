package cl.creando.skappserver.common.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String profilePicture;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private UUID clientId;
    private List<UUID> roleIds;
}

