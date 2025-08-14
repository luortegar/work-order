package cl.creando.skappserver.workorder.response;

import cl.creando.skappserver.workorder.entity.Client;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ClientResponse {
    private String clientId;
    private String companyName;
    private String uniqueTaxpayerIdentification;
    private String business;
    private String address;
    private String commune;
    private String city;
    private String typeOfPurchase;

    public ClientResponse(Client client) {
        this.clientId = client.getClientId().toString();
        this.companyName = client.getCompanyName();
        this.uniqueTaxpayerIdentification = client.getUniqueTaxpayerIdentification();
        this.business = client.getBusiness();
        this.address = client.getAddress();
        this.commune = client.getCommune();
        this.city = client.getCity();
        this.typeOfPurchase = client.getTypeOfPurchase().name();
    }
}
