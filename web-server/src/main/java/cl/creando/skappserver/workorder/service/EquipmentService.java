package cl.creando.skappserver.workorder.service;

import cl.creando.skappserver.common.CommonFunctions;
import cl.creando.skappserver.common.exception.SKException;
import cl.creando.skappserver.workorder.entity.Branch;
import cl.creando.skappserver.workorder.entity.Equipment;
import cl.creando.skappserver.workorder.entity.EquipmentType;
import cl.creando.skappserver.workorder.repository.BranchRepository;
import cl.creando.skappserver.workorder.repository.EquipmentRepository;
import cl.creando.skappserver.workorder.repository.EquipmentTypeRepository;
import cl.creando.skappserver.workorder.request.EquipmentRequest;
import cl.creando.skappserver.workorder.response.EquipmentAutocompleteResponse;
import cl.creando.skappserver.workorder.response.EquipmentResponse;
import cl.creando.skappserver.workorder.response.EquipmentTypeResponse;
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
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final BranchRepository branchRepository;
    private final EquipmentTypeRepository equipmentTypeRepository;

    public Page<?> findAll(UUID branchId, String searchTerm, Pageable pageable) {
        Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new SKException("Item not found.", HttpStatus.NOT_FOUND));
        Specification<Equipment> specification = ((root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("updateDate")));
            Predicate predicateOr = criteriaBuilder.or(
                    criteriaBuilder.like(root.get("equipmentId").as(String.class), CommonFunctions.getPattern(searchTerm)),
                    criteriaBuilder.like(root.get("equipmentModel"), CommonFunctions.getPattern(searchTerm)),
                    criteriaBuilder.like(root.get("equipmentBrand"), CommonFunctions.getPattern(searchTerm)),
                    criteriaBuilder.like(root.get("serialNumber"), CommonFunctions.getPattern(searchTerm)));
            return criteriaBuilder.and(predicateOr, criteriaBuilder.equal(root.get("branch"), branch));
        });
        Page<Equipment> all = equipmentRepository.findAll(specification, pageable);

        List<EquipmentResponse> list = all.map(EquipmentResponse::new).stream().toList();
        return new PageImpl<>(list, all.getPageable(), all.getTotalElements());
    }

    public EquipmentResponse findById(UUID id) {
        return new EquipmentResponse(equipmentRepository.findById(id).orElseThrow(() -> new SKException("Item  not found.", HttpStatus.NOT_FOUND)));
    }

    public EquipmentResponse add(EquipmentRequest request) {
        Branch branch = branchRepository.findById(request.getBranchId()).orElseThrow(() -> new SKException("Branch  not found.", HttpStatus.BAD_REQUEST));
        Equipment equipment = new Equipment();

        if(request.getEquipmentTypeId()!=null) {
            EquipmentType equipmentType = equipmentTypeRepository.findById(request.getEquipmentTypeId()).orElseThrow(() -> new SKException("Equipment type not found.", HttpStatus.BAD_REQUEST));
            equipment.setEquipmentType(equipmentType);
        }
        request.setValues(equipment);
        equipment.setBranch(branch);
        equipment = equipmentRepository.save(equipment);
        return new EquipmentResponse(equipment);
    }

    public EquipmentResponse edit(UUID id, EquipmentRequest request) {
        Equipment equipment = equipmentRepository.findById(id).orElseThrow(() -> new SKException("Item  not found.", HttpStatus.NOT_FOUND));
        Branch branch = branchRepository.findById(request.getBranchId()).orElseThrow(() -> new SKException("Branch  not found.", HttpStatus.BAD_REQUEST));
        if(request.getEquipmentTypeId()!=null) {
            EquipmentType equipmentType = equipmentTypeRepository.findById(request.getEquipmentTypeId()).orElseThrow(() -> new SKException("Equipment type not found.", HttpStatus.BAD_REQUEST));
            equipment.setEquipmentType(equipmentType);
        }
        else{
            equipment.setEquipmentType(null);
        }
        request.setValues(equipment);
        equipment.setBranch(branch);
        equipment = equipmentRepository.save(equipment);
        return new EquipmentResponse(equipment);
    }

    public EquipmentResponse delete(UUID id) {
        Equipment equipment = equipmentRepository.findById(id).orElseThrow(() -> new SKException("Item  not found.", HttpStatus.NOT_FOUND));
        equipment = equipmentRepository.save(equipment);
        equipmentRepository.delete(equipment);
        return new EquipmentResponse(equipment);
    }

    public List<EquipmentTypeResponse> autoCompleteEquipmentType(String searchTerm) {
        List<EquipmentType> equipmentTypeList = equipmentTypeRepository.findTop10ByTypeNameContainingIgnoreCase(searchTerm);
        return equipmentTypeList.stream().map(EquipmentTypeResponse::new).toList();
    }

    public Object autoCompleteEquipment(UUID branchId, String searchTerm) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new SKException("Item not found.", HttpStatus.NOT_FOUND));

        // Definimos el Pageable para traer solo los primeros 10 resultados
        Pageable pageable = PageRequest.of(0, 10);

        Specification<Equipment> specification = (root, query, criteriaBuilder) -> {
            Predicate searchPredicate = criteriaBuilder.or(
                    criteriaBuilder.like(root.get("equipmentId").as(String.class), CommonFunctions.getPattern(searchTerm)),
                    criteriaBuilder.like(root.get("equipmentModel"), CommonFunctions.getPattern(searchTerm)),
                    criteriaBuilder.like(root.get("equipmentBrand"), CommonFunctions.getPattern(searchTerm)),
                    criteriaBuilder.like(root.get("serialNumber"), CommonFunctions.getPattern(searchTerm))
            );
            return criteriaBuilder.and(searchPredicate, criteriaBuilder.equal(root.get("branch"), branch));
        };

        // Usamos findAll con la especificación y el Pageable
        Page<Equipment> equipmentPage = equipmentRepository.findAll(specification, pageable);

        // Mapeamos los resultados a EquipmentResponse
        return equipmentPage.getContent().stream()
                .map(EquipmentAutocompleteResponse::new)
                .toList();
    }
}
