package cl.creando.skappserver.workorder.service;

import cl.creando.skappserver.common.CommonFunctions;
import cl.creando.skappserver.common.exception.SKException;
import cl.creando.skappserver.workorder.entity.Client;
import cl.creando.skappserver.workorder.repository.ClientRepository;
import cl.creando.skappserver.workorder.request.ClientRequest;
import cl.creando.skappserver.workorder.response.ClientResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ClientService {

    @Autowired
    private final ClientRepository clientRepository;

    public Page<?> findAll(String searchTerm, Pageable pageable) {
        Specification<Client> specification = (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("updateDate")));
            return criteriaBuilder.or(
                    criteriaBuilder.like(root.get("companyName"), CommonFunctions.getPattern(searchTerm)),
                    criteriaBuilder.like(root.get("uniqueTaxpayerIdentification"), CommonFunctions.getPattern(searchTerm)),
                    criteriaBuilder.like(root.get("business"), CommonFunctions.getPattern(searchTerm)),
                    criteriaBuilder.like(root.get("address"), CommonFunctions.getPattern(searchTerm)),
                    criteriaBuilder.like(root.get("commune"), CommonFunctions.getPattern(searchTerm)),
                    criteriaBuilder.like(root.get("city"), CommonFunctions.getPattern(searchTerm))
            );
        };
        Page<Client> all = clientRepository.findAll(specification, pageable);

        List<ClientResponse> list = all.map(ClientResponse::new).stream().toList();
        return new PageImpl<>(list, all.getPageable(), all.getTotalElements());
    }

    public ClientResponse findById(UUID id) {
        return new ClientResponse(clientRepository.findById(id).orElseThrow(() -> new SKException("Item  not found.", HttpStatus.NOT_FOUND)));
    }

    public ClientResponse add(ClientRequest request) {
        Client client = new Client();
        request.setValues(client);
        client = clientRepository.save(client);
        return new ClientResponse(client);
    }

    public ClientResponse edit(UUID id, ClientRequest request) {
        Client client = clientRepository.findById(id).orElseThrow(() -> new SKException("Item  not found.", HttpStatus.NOT_FOUND));
        request.setValues(client);
        client = clientRepository.save(client);
        return new ClientResponse(client);
    }

    public ClientResponse delete(UUID id) {
        Client client = clientRepository.findById(id).orElseThrow(() -> new SKException("Item  not found.", HttpStatus.NOT_FOUND));
        client = clientRepository.save(client);
        clientRepository.delete(client);
        return new ClientResponse(client);
    }
}
