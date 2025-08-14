package cl.creando.skappserver.common.response;

import cl.creando.skappserver.common.entity.address.Commune;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommuneResponse {
    private String country, region, commune;

    public CommuneResponse(Commune commune) {
        setCommune(commune.getName());
        setRegion(commune.getRegion().getName());
        setCountry(commune.getRegion().getCountry().getName());
    }
}
