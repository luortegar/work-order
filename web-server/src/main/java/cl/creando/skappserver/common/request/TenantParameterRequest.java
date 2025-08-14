package cl.creando.skappserver.common.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TenantParameterRequest {
    @NotBlank
    private String parameterName;
    @NotBlank
    private String parameterValue;
}
