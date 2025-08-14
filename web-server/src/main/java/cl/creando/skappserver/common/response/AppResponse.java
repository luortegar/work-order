package cl.creando.skappserver.common.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AppResponse {
    private String message;
    private Integer statusCode;
    private Date responseDate;
}
