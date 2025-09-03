package cl.creando.skappserver.workorder.service;


import cl.creando.skappserver.common.CommonFunctions;
import cl.creando.skappserver.common.entity.common.File;
import cl.creando.skappserver.common.entity.user.User;
import cl.creando.skappserver.common.exception.SKException;
import cl.creando.skappserver.common.repository.FileRepository;
import cl.creando.skappserver.common.repository.UserRepository;
import cl.creando.skappserver.workorder.entity.*;
import cl.creando.skappserver.workorder.repository.BranchRepository;
import cl.creando.skappserver.workorder.repository.EquipmentRepository;
import cl.creando.skappserver.workorder.repository.WorkOrderRepository;
import cl.creando.skappserver.workorder.request.WorkOrderRequest;
import cl.creando.skappserver.workorder.response.DetailedWorkOrderResponse;
import cl.creando.skappserver.workorder.response.WorkOrderResponse;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WorkOrderService {

    private final WorkOrderRepository workOrderRepository;
    private final WorkOrderPdfGenerator pdfGenerator;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;
    private final FileRepository fileRepository;


    public List<WorkOrder> findAll() {
        return workOrderRepository.findAll();
    }

    public ResponseEntity<?> findAllOld(String searchTerm, WorkOrderStatus workOrderStatus, Pageable pageable) {
        Specification<WorkOrder> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = List.of(
                    criteriaBuilder.like(root.get("workOrderId").as(String.class), CommonFunctions.getPattern(searchTerm)),
                    criteriaBuilder.like(root.get("workOrderNumber"), CommonFunctions.getPattern(searchTerm))
            );
            //noinspection ToArrayCallWithZeroLengthArrayArgument
            return criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<WorkOrder> all = workOrderRepository.findAll(specification, pageable);
        List<WorkOrderResponse> list = all.map(WorkOrderResponse::new).stream().toList();
        //noinspection unchecked,rawtypes
        Page page = new PageImpl(list, all.getPageable(), all.getTotalElements());
        return ResponseEntity.ok(page);
    }

    public ResponseEntity<?> findAll(String searchTerm, WorkOrderStatus workOrderStatus, Pageable pageable) {
        Specification<WorkOrder> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Search by ID or Number
            predicates.add(criteriaBuilder.like(root.get("workOrderId").as(String.class), CommonFunctions.getPattern(searchTerm)));
            predicates.add(criteriaBuilder.like(root.get("workOrderNumber"), CommonFunctions.getPattern(searchTerm)));
            // Combine OR for searchTerm
            Predicate searchPredicate = criteriaBuilder.or(predicates.toArray(new Predicate[0]));
            // Final predicate list
            List<Predicate> finalPredicates = new ArrayList<>();
            finalPredicates.add(searchPredicate);
            // Add AND filter for workOrderStatus if not null
            if (workOrderStatus != null) {
                finalPredicates.add(criteriaBuilder.equal(root.get("workOrderStatus"), workOrderStatus));
            }
            return criteriaBuilder.and(finalPredicates.toArray(new Predicate[0]));
        };

        Page<WorkOrder> all = workOrderRepository.findAll(specification, pageable);
        List<WorkOrderResponse> list = all.map(WorkOrderResponse::new).stream().toList();

        Page<WorkOrderResponse> page = new PageImpl<>(list, all.getPageable(), all.getTotalElements());
        return ResponseEntity.ok(page);
    }


    public DetailedWorkOrderResponse findById(UUID id) {
        return new DetailedWorkOrderResponse(workOrderRepository.findById(id).orElseThrow(() -> new SKException("Invalid id.", HttpStatus.NOT_FOUND)));
    }

    public WorkOrderResponse save(WorkOrderRequest workOrderRequest) {
        WorkOrder newWorkOrder = new WorkOrder();

        // Campos simples
        newWorkOrder.setWorkOrderNumber(workOrderRequest.getWorkOrderNumber());
        newWorkOrder.setServiceDetails(workOrderRequest.getServiceDetails());
        newWorkOrder.setObservations(workOrderRequest.getObservations());
        newWorkOrder.setStartTime(workOrderRequest.getStartTime());
        newWorkOrder.setEndTime(workOrderRequest.getEndTime());

        // Branch
        Branch branch = branchRepository.findById(workOrderRequest.getBranchId())
                .orElseThrow(() -> new SKException("Invalid branchId.", HttpStatus.BAD_REQUEST));
        newWorkOrder.setBranch(branch);

        // Recipient
        if (workOrderRequest.getRecipientId() != null) {
            User recipient = userRepository.findById(workOrderRequest.getRecipientId())
                    .orElseThrow(() -> new SKException("Invalid recipientId.", HttpStatus.BAD_REQUEST));
            newWorkOrder.setRecipient(recipient);
        }

        // Technician principal
        if (workOrderRequest.getTechnicianId() != null) {
            User technician = userRepository.findById(workOrderRequest.getTechnicianId())
                    .orElseThrow(() -> new SKException("Invalid technicianId.", HttpStatus.BAD_REQUEST));
            newWorkOrder.setTechnician(technician);
        }

        // Equipment principal
        if (workOrderRequest.getEquipmentId() != null) {
            Equipment equipment = equipmentRepository.findById(workOrderRequest.getEquipmentId())
                    .orElseThrow(() -> new SKException("Invalid equipmentId.", HttpStatus.BAD_REQUEST));
            newWorkOrder.setEquipment(equipment);
        }

        // Lista de técnicos adicionales
        if (workOrderRequest.getTechnicianIdList() != null && !workOrderRequest.getTechnicianIdList().isEmpty()) {
            List<WorkOrderTechnician> workOrderTechnicians = workOrderRequest.getTechnicianIdList().stream()
                    .map(techId -> {
                        User tech = userRepository.findById(techId)
                                .orElseThrow(() -> new SKException("Invalid technicianId in list.", HttpStatus.BAD_REQUEST));
                        WorkOrderTechnician wot = new WorkOrderTechnician();
                        wot.setWorkOrder(newWorkOrder);
                        wot.setTechnician(tech);
                        return wot;
                    }).collect(Collectors.toList());
            newWorkOrder.setWorkOrderTechnicianList(workOrderTechnicians);
        }

        // Lista de fotos
        if (workOrderRequest.getPhotoIdList() != null && !workOrderRequest.getPhotoIdList().isEmpty()) {
            List<WorkOrderPhoto> photos = workOrderRequest.getPhotoIdList().stream()
                    .map(photoId -> {
                        File file = fileRepository.findById(photoId).orElseThrow(()-> new SKException("Invalid file id", HttpStatus.BAD_REQUEST));
                        WorkOrderPhoto photo = new WorkOrderPhoto();
                        photo.setWorkOrder(newWorkOrder);
                        photo.setFile(file);
                        return photo;
                    }).collect(Collectors.toList());
            newWorkOrder.setFileList(photos);
        }

        // Lista de equipos adicionales (si aplica)
        if (workOrderRequest.getEquipmentIdList() != null && !workOrderRequest.getEquipmentIdList().isEmpty()) {
            for (UUID eqId : workOrderRequest.getEquipmentIdList()) {
                Equipment eq = equipmentRepository.findById(eqId)
                        .orElseThrow(() -> new SKException("Invalid equipmentId in list.", HttpStatus.BAD_REQUEST));
                // Aquí podrías crear una entidad intermedia si existe (WorkOrderEquipment) o manejarlo como corresponda
            }
        }

        // Guardar y devolver respuesta
        WorkOrder saved = workOrderRepository.save(newWorkOrder);
        return new WorkOrderResponse(saved);
    }


    public WorkOrderResponse deleteById(UUID id) {
        WorkOrder workOrder = workOrderRepository.findById(id).orElseThrow(() -> new SKException("Invalid id.", HttpStatus.NOT_FOUND));
        workOrderRepository.delete(workOrder);
        return new WorkOrderResponse(workOrder);
    }

    public WorkOrderResponse updateWorkOrder(UUID id, WorkOrderRequest workOrderRequest) {
        WorkOrder oldWorkOrder = workOrderRepository.getReferenceById(id);

        WorkOrder workOrder = null;

        return new WorkOrderResponse (workOrderRepository.save(workOrder));
    }

    public ResponseEntity<Resource> generatePDF(UUID id) throws IOException {
        WorkOrder oldWorkOrder = workOrderRepository.getReferenceById(id);

        InputStream inputStream = pdfGenerator.generateWorkOrderPdf(oldWorkOrder);

        InputStreamResource file = new InputStreamResource(inputStream);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "ot.pdf")
                .contentType(MediaType.APPLICATION_PDF).body(file);
    }
}
