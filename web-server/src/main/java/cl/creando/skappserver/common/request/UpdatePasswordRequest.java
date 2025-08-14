package cl.creando.skappserver.common.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdatePasswordRequest {
    private String currentPassword;
    private String newPassword;
}
