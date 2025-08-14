package cl.creando.skappserver.workorder.response;

import cl.creando.skappserver.workorder.entity.TypeOfPurchase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TypeOfPurchaseResponse {
    private String name;
    private String code;

    public TypeOfPurchaseResponse(TypeOfPurchase typeOfPurchase) {
        this.name = typeOfPurchase.getName();
        this.code = typeOfPurchase.name();
    }
}
