package cl.creando.skappserver.workorder.service;


import cl.creando.skappserver.common.CommonFunctions;
import cl.creando.skappserver.common.entity.common.File;
import cl.creando.skappserver.common.entity.user.User;
import cl.creando.skappserver.common.exception.SKException;
import cl.creando.skappserver.common.repository.UserRepository;
import cl.creando.skappserver.common.response.FileResponse;
import cl.creando.skappserver.common.response.GenericResponse;
import cl.creando.skappserver.common.service.FileService;
import cl.creando.skappserver.workorder.entity.*;
import cl.creando.skappserver.workorder.repository.BranchRepository;
import cl.creando.skappserver.workorder.repository.EquipmentRepository;
import cl.creando.skappserver.workorder.repository.WorkOrderPhotoRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final WorkOrderPhotoRepository workOrderPhotoRepository;
    private final FileService fileService;

    public ResponseEntity<?> findAll(String searchTerm, WorkOrderStatus workOrderStatus, Pageable pageable) {
        Specification<WorkOrder> specification = (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("updateDate")));
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

        WorkOrder workOrder = workOrderRepository.findById(id).orElseThrow(() -> new SKException("Invalid id.", HttpStatus.NOT_FOUND));
        String signatureRecipientAsBase64 = workOrder.getSignatureRecipient() != null ? fileService.getFileAsBase64(workOrder.getSignatureRecipient().getFileId()) : null;
        String signatureTechnicianAsBase64 = workOrder.getSignatureTechnician() != null ? fileService.getFileAsBase64(workOrder.getSignatureTechnician().getFileId()) : null;

        List<WorkOrderPhoto> workOrderFileList = workOrder.getFileList();
        List<FileResponse> fileResponseList = workOrderFileList.stream().map(f -> fileService.getFileResponse(f.getWorkOrderPhotoId(), f.getFile())).toList();

        DetailedWorkOrderResponse detailedWorkOrderResponse = new DetailedWorkOrderResponse(workOrder);
        detailedWorkOrderResponse.setRecipientSignatureBase64(signatureRecipientAsBase64);
        detailedWorkOrderResponse.setTechnicianSignatureBase64(signatureTechnicianAsBase64);
        detailedWorkOrderResponse.setListOfPhotos(fileResponseList);

        return detailedWorkOrderResponse;
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
        if (workOrderRequest.getBranchId() != null) {
            Branch branch = branchRepository.findById(workOrderRequest.getBranchId())
                    .orElseThrow(() -> new SKException("Invalid branchId.", HttpStatus.BAD_REQUEST));
            newWorkOrder.setBranch(branch);
        }
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
                        File file = fileService.findById(photoId).orElseThrow(()-> new SKException("Invalid file id", HttpStatus.BAD_REQUEST));
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

    @Transactional
    public WorkOrderResponse updateWorkOrder(UUID id, WorkOrderRequest workOrderRequest) {
        WorkOrder oldWorkOrder = workOrderRepository.findById(id)
                .orElseThrow(() -> new SKException("WorkOrder not found", HttpStatus.NOT_FOUND));

        // Campos simples
        oldWorkOrder.setWorkOrderNumber(workOrderRequest.getWorkOrderNumber());
        oldWorkOrder.setServiceDetails(workOrderRequest.getServiceDetails());
        oldWorkOrder.setObservations(workOrderRequest.getObservations());
        oldWorkOrder.setStartTime(workOrderRequest.getStartTime());
        oldWorkOrder.setEndTime(workOrderRequest.getEndTime());

        // Branch
        if (workOrderRequest.getBranchId() != null) {
            Branch branch = branchRepository.findById(workOrderRequest.getBranchId())
                    .orElseThrow(() -> new SKException("Invalid branchId.", HttpStatus.BAD_REQUEST));
            oldWorkOrder.setBranch(branch);
        }
        // Recipient
        if (workOrderRequest.getRecipientId() != null) {
            User recipient = userRepository.findById(workOrderRequest.getRecipientId())
                    .orElseThrow(() -> new SKException("Invalid recipientId.", HttpStatus.BAD_REQUEST));
            oldWorkOrder.setRecipient(recipient);
        } else {
            oldWorkOrder.setRecipient(null); // permite limpiar si viene vacío
        }

        // Technician principal
        if (workOrderRequest.getTechnicianId() != null) {
            User technician = userRepository.findById(workOrderRequest.getTechnicianId())
                    .orElseThrow(() -> new SKException("Invalid technicianId.", HttpStatus.BAD_REQUEST));
            oldWorkOrder.setTechnician(technician);
        } else {
            oldWorkOrder.setTechnician(null);
        }

        // Equipment principal
        if (workOrderRequest.getEquipmentId() != null) {
            Equipment equipment = equipmentRepository.findById(workOrderRequest.getEquipmentId())
                    .orElseThrow(() -> new SKException("Invalid equipmentId.", HttpStatus.BAD_REQUEST));
            oldWorkOrder.setEquipment(equipment);
        } else {
            oldWorkOrder.setEquipment(null);
        }

        // Lista de técnicos adicionales
        oldWorkOrder.getWorkOrderTechnicianList().clear();
        if (workOrderRequest.getTechnicianIdList() != null && !workOrderRequest.getTechnicianIdList().isEmpty()) {
            List<WorkOrderTechnician> workOrderTechnicians = workOrderRequest.getTechnicianIdList().stream()
                    .map(techId -> {
                        User tech = userRepository.findById(techId)
                                .orElseThrow(() -> new SKException("Invalid technicianId in list.", HttpStatus.BAD_REQUEST));
                        WorkOrderTechnician wot = new WorkOrderTechnician();
                        wot.setWorkOrder(oldWorkOrder);
                        wot.setTechnician(tech);
                        return wot;
                    }).collect(Collectors.toList());
            oldWorkOrder.getWorkOrderTechnicianList().addAll(workOrderTechnicians);
        }


        // Lista de equipos adicionales (si aplica)
        if (workOrderRequest.getEquipmentIdList() != null && !workOrderRequest.getEquipmentIdList().isEmpty()) {
            // TODO: manejar WorkOrderEquipment si lo modelaste como entidad intermedia
        }

        if (oldWorkOrder.getSignatureRecipient() != null) {
            fileService.deleteFile(oldWorkOrder.getSignatureRecipient().getFileId());
        }

        if (oldWorkOrder.getSignatureTechnician() != null) {
            fileService.deleteFile(oldWorkOrder.getSignatureTechnician().getFileId());
        }

        String recipientSignatureBase64 = workOrderRequest.getRecipientSignatureBase64();
        if(recipientSignatureBase64 != null) {
            File recipientSignatureFile = fileService.saveFileBase64(recipientSignatureBase64, "recipientSignature.png");
            oldWorkOrder.setSignatureRecipient(recipientSignatureFile);
        }
        String technicianSignatureBase64 = workOrderRequest.getTechnicianSignatureBase64();
        if(technicianSignatureBase64 !=null) {
            File technicianSignatureFile = fileService.saveFileBase64(technicianSignatureBase64, "technicianSignature.png");
            oldWorkOrder.setSignatureTechnician(technicianSignatureFile);
        }

        // Guardar y devolver respuesta
        WorkOrder updated = workOrderRepository.save(oldWorkOrder);
        return new WorkOrderResponse(updated);
    }


    public ResponseEntity<Resource> generatePDFDownload(UUID id) throws IOException {
        InputStreamResource file = generatePDFInputStreamResource(id);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "ot.pdf")
                .contentType(MediaType.APPLICATION_PDF).body(file);
    }

    private InputStreamResource generatePDFInputStreamResource(UUID id) throws IOException {
        WorkOrder oldWorkOrder = workOrderRepository.findById(id).orElseThrow(()->new SKException("Invalid work order.", HttpStatus.BAD_REQUEST));

        InputStream inputStream = pdfGenerator.generateWorkOrderPdf(oldWorkOrder);

        InputStreamResource file = new InputStreamResource(inputStream);
        return file;
    }

    public GenericResponse updatePhotoToWorkOrder(UUID id, MultipartFile request) {
        WorkOrder workOrder = workOrderRepository.findById(id).orElseThrow(() -> new SKException("Invalid id.", HttpStatus.NOT_FOUND));
        try {
            File file = fileService.getSavedFile(request);
            WorkOrderPhoto workOrderPhoto = new WorkOrderPhoto();
            workOrderPhoto.setWorkOrder(workOrder);
            workOrderPhoto.setFile(file);
            workOrderPhotoRepository.save(workOrderPhoto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return GenericResponse.builder().message("File upload successful.").build();

    }

    public GenericResponse deletePhotoToWorkOrder(UUID id, UUID workOrderPhotoId) {

        WorkOrderPhoto workOrderPhoto = workOrderPhotoRepository.findById(workOrderPhotoId).orElseThrow(() -> new SKException("Work Order Photo not found.", HttpStatus.NOT_FOUND));
        File file = workOrderPhoto.getFile();
        workOrderPhotoRepository.delete(workOrderPhoto);
        fileService.deleteFile(file.getFileId());
        return GenericResponse.builder().message("File deleted successful.").build();
    }

    public Object viewPhotosOfAWorkOrder(UUID id) {
        WorkOrder workOrder = workOrderRepository.findById(id).orElseThrow(() -> new SKException("Invalid id.", HttpStatus.NOT_FOUND));
        List<WorkOrderPhoto> workOrderFileList = workOrder.getFileList();
        return workOrderFileList.stream().map(f -> fileService.getFileResponse(f.getWorkOrderPhotoId(), f.getFile())).toList();
    }

    public ResponseEntity<Resource> generatePDFPreview(UUID id) throws IOException {
        InputStreamResource file = generatePDFInputStreamResource(id);


        ResponseEntity<Resource>  response = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .contentType(MediaType.APPLICATION_PDF)
                .body(file);

        HttpHeaders headers = new HttpHeaders();
        headers.putAll(response.getHeaders());
        headers.remove("X-Frame-Options");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(response.getBody());
    }
}
