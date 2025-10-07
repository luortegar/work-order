package cl.creando.skappserver.workorder.service;

import cl.creando.skappserver.common.CommonFunctions;
import cl.creando.skappserver.common.exception.SKException;
import cl.creando.skappserver.workorder.entity.Branch;
import cl.creando.skappserver.workorder.entity.Client;
import cl.creando.skappserver.workorder.repository.BranchRepository;
import cl.creando.skappserver.workorder.repository.ClientRepository;
import cl.creando.skappserver.workorder.request.BranchRequest;
import cl.creando.skappserver.workorder.response.BranchAutocompleteResponse;
import cl.creando.skappserver.workorder.response.BranchResponse;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;
    private final ClientRepository clientRepository;


    public Page<?> findAll(UUID clientId, String searchTerm, Pageable pageable) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new SKException("Client not found.", HttpStatus.NOT_FOUND));
        Specification<Branch> specification = ((root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("updateDate")));
            Predicate predicateOr = criteriaBuilder.or(

                    criteriaBuilder.like(root.get("branchId").as(String.class), CommonFunctions.getPattern(searchTerm)),
                    criteriaBuilder.like(root.get("branchName"), CommonFunctions.getPattern(searchTerm)),
                    criteriaBuilder.like(root.get("address"), CommonFunctions.getPattern(searchTerm)),
                    criteriaBuilder.like(root.get("commune"), CommonFunctions.getPattern(searchTerm))
            );
            return criteriaBuilder.and(predicateOr, criteriaBuilder.equal(root.get("client"), client));
        });
        Page<Branch> all = branchRepository.findAll(specification, pageable);

        List<BranchResponse> list = all.map(BranchResponse::new).stream().toList();
        return new PageImpl<>(list, all.getPageable(), all.getTotalElements());
    }

    public BranchResponse findById(UUID id) {
        return new BranchResponse(branchRepository.findById(id).orElseThrow(() -> new SKException("Item  not found.", HttpStatus.NOT_FOUND)));
    }

    public BranchResponse add(BranchRequest request) {
        Client client = clientRepository.findById(request.getClientId()).orElseThrow(() -> new SKException("Client not found.", HttpStatus.NOT_FOUND));
        Branch branch = new Branch();
        request.setValues(branch);
        branch.setClient(client);
        branch = branchRepository.save(branch);
        return new BranchResponse(branch);
    }

    public BranchResponse edit(UUID id, BranchRequest request) {
        Client client = clientRepository.findById(request.getClientId()).orElseThrow(() -> new SKException("Client not found.", HttpStatus.NOT_FOUND));
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new SKException("Item  not found.", HttpStatus.NOT_FOUND));
        request.setValues(branch);
        branch.setClient(client);
        branch = branchRepository.save(branch);
        return new BranchResponse(branch);
    }

    public BranchResponse delete(UUID id) {
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new SKException("Item  not found.", HttpStatus.NOT_FOUND));
        branch = branchRepository.save(branch);
        branchRepository.delete(branch);
        return new BranchResponse(branch);
    }

    public List<BranchAutocompleteResponse> autocomplete(String searchTerm) {
        Specification<Branch> specification = (root, query, cb) -> {
            Join<Branch, Client> clientJoin = root.join("client", JoinType.LEFT);

            String pattern = CommonFunctions.getPattern(searchTerm.trim());

            return cb.or(
                    cb.like(cb.lower(root.get("branchId").as(String.class)), pattern.toLowerCase()),
                    cb.like(cb.lower(root.get("branchName")), pattern.toLowerCase()),
                    cb.like(cb.lower(root.get("address")), pattern.toLowerCase()),
                    cb.like(cb.lower(clientJoin.get("companyName")), pattern.toLowerCase())
            );
        };

        Pageable limit = PageRequest.of(0, 10); // Solo 10 resultados

        List<Branch> branches = branchRepository.findAll(specification, limit).getContent();

        return branches.stream().map(BranchAutocompleteResponse::new).toList();
    }

}
