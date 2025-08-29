package cl.creando.skappserver.workorder.response;

import cl.creando.skappserver.common.entity.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserAutocompleteResponse {

    private UUID employeeId;
    private String employeeFullName;


    public UserAutocompleteResponse(User user) {
        this.employeeId = user.getUserId();
        this.employeeFullName = String.format("%s - %s (%s)",user.getFirstName(), user.getLastName(), user.getEmail());
    }
}
