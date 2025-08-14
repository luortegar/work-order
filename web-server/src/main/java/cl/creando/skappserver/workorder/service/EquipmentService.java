package cl.creando.skappserver.workorder.service;

import cl.creando.skappserver.common.CommonFunctions;
import cl.creando.skappserver.common.exception.SKException;
import cl.creando.skappserver.workorder.entity.Branch;
import cl.creando.skappserver.workorder.entity.Equipment;
import cl.creando.skappserver.workorder.repository.BranchRepository;
import cl.creando.skappserver.workorder.repository.EquipmentRepository;
import cl.creando.skappserver.workorder.request.EquipmentRequest;
import cl.creando.skappserver.workorder.response.EquipmentResponse;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final BranchRepository branchRepository;

    public Page<?> findAll(UUID branchId, String searchTerm, Pageable pageable) {
        Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new SKException("Item not found.", HttpStatus.NOT_FOUND));
        Specification<Equipment> specification = ((root, query, criteriaBuilder) -> {
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
        request.setValues(equipment);
        equipment.setBranch(branch);
        equipment = equipmentRepository.save(equipment);
        return new EquipmentResponse(equipment);
    }

    public EquipmentResponse edit(UUID id, EquipmentRequest request) {
        Equipment equipment = equipmentRepository.findById(id).orElseThrow(() -> new SKException("Item  not found.", HttpStatus.NOT_FOUND));
        Branch branch = branchRepository.findById(request.getBranchId()).orElseThrow(() -> new SKException("Branch  not found.", HttpStatus.BAD_REQUEST));
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
}
