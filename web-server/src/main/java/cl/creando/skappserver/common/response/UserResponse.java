package cl.creando.skappserver.common.response;

import cl.creando.skappserver.common.entity.user.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.UUID;

@Data
public class UserResponse {
    private UUID userId;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String profilePictureUrl;

    public UserResponse(User user, String host) {

        this(user);
        if( user.getProfilePictureFileId() != null && host != null) {
            this.profilePictureUrl = host + "/public/v1/files/" + user.getProfilePictureFileId().getFileId().toString() + "/download";
        }
    }

    public UserResponse(User user) {
        this.userId = user.getUserId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.fullName = user.getFirstName() + " " + user.getLastName();
    }

}
