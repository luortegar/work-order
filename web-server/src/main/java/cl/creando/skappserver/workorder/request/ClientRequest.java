package cl.creando.skappserver.workorder.request;

import cl.creando.skappserver.workorder.entity.Client;
import cl.creando.skappserver.workorder.entity.TypeOfPurchase;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;


@Setter
@Getter
public class ClientRequest {
    private String companyName;
    private String uniqueTaxpayerIdentification;
    private String business;
    private String address;
    private String commune;
    private String city;
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private TypeOfPurchase typeOfPurchase;

    public void setValues(Client client) {
        client.setCompanyName(this.companyName);
        client.setUniqueTaxpayerIdentification(this.uniqueTaxpayerIdentification);
        client.setBusiness(this.business);
        client.setAddress(this.address);
        client.setCommune(this.commune);
        client.setCity(this.city);
        client.setTypeOfPurchase(this.typeOfPurchase);
    }
}
