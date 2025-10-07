package cl.creando.skappserver.workorder.service;

import cl.creando.skappserver.common.CommonFunctions;
import cl.creando.skappserver.common.entity.common.File;
import cl.creando.skappserver.common.exception.SKException;
import cl.creando.skappserver.common.response.GenericResponse;
import cl.creando.skappserver.common.service.FileService;
import cl.creando.skappserver.workorder.entity.*;
import cl.creando.skappserver.workorder.repository.BranchRepository;
import cl.creando.skappserver.workorder.repository.InspectionVisitRepository;
import cl.creando.skappserver.workorder.repository.VisitPhotoRepository;
import cl.creando.skappserver.workorder.request.InspectionVisitRequest;
import cl.creando.skappserver.workorder.response.DetailedInspectionVisitResponse;
import cl.creando.skappserver.workorder.response.InspectionVisitResponse;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class InspectionVisitService {

    private final InspectionVisitRepository inspectionVisitRepository;
    private final BranchRepository branchRepository;
    private final FileService fileService;
    private final VisitPhotoRepository visitPhotoRepository;

    public Page<?> findAll(UUID branchId, String searchTerm, Pageable pageable) {
        Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new SKException("Item not found.", HttpStatus.NOT_FOUND));
        Specification<InspectionVisit> specification = ((root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("updateDate")));
            Predicate predicateOr = criteriaBuilder.or(
                    criteriaBuilder.like(root.get("inspectionVisitId").as(String.class), CommonFunctions.getPattern(searchTerm)),
                    criteriaBuilder.like(root.get("title"), CommonFunctions.getPattern(searchTerm)),
                    criteriaBuilder.like(root.get("descriptions"), CommonFunctions.getPattern(searchTerm)));
            return criteriaBuilder.and(predicateOr, criteriaBuilder.equal(root.get("branch"), branch));
        });
        Page<InspectionVisit> all = inspectionVisitRepository.findAll(specification, pageable);

        List<InspectionVisitResponse> list = all.map(InspectionVisitResponse::new).stream().toList();
        return new PageImpl<>(list, all.getPageable(), all.getTotalElements());
    }

    public Object findById(UUID id) {
        InspectionVisit inspectionVisit = inspectionVisitRepository.findById(id).orElseThrow(()->new SKException("Invalid inspectionVisitId", HttpStatus.NOT_FOUND));
        return new DetailedInspectionVisitResponse(inspectionVisit);
    }

    public Object save(InspectionVisitRequest inspectionVisitRequest) {
        Branch branch = branchRepository.findById(inspectionVisitRequest.getBranchId()).orElseThrow(() -> new SKException("Item not found.", HttpStatus.NOT_FOUND));
        InspectionVisit inspectionVisit = new InspectionVisit();
        inspectionVisit.setBranch(branch);
        inspectionVisit.setTitle(inspectionVisitRequest.getTitle());
        inspectionVisit.setDescriptions(inspectionVisitRequest.getDescriptions());
        inspectionVisit.setDate(inspectionVisitRequest.getDate());
        inspectionVisitRepository.save(inspectionVisit);
        return new GenericResponse("Created inspectionVisit successful.");
    }

    public Object update(UUID id, InspectionVisitRequest inspectionVisitRequest) {
        InspectionVisit inspectionVisit = inspectionVisitRepository.findById(id).orElseThrow(()->new SKException("Invalid inspectionVisitId", HttpStatus.NOT_FOUND));
        inspectionVisit.setTitle(inspectionVisitRequest.getTitle());
        inspectionVisit.setDescriptions(inspectionVisitRequest.getDescriptions());
        inspectionVisit.setDate(inspectionVisitRequest.getDate());
        inspectionVisitRepository.save(inspectionVisit);
        return new GenericResponse("Updated inspectionVisit successful.");
    }

    public Object deleteById(UUID id) {
        InspectionVisit inspectionVisit = inspectionVisitRepository.findById(id)
                .orElseThrow(() -> new SKException("Invalid inspectionVisitId", HttpStatus.NOT_FOUND));

        List<VisitPhoto> photoList = inspectionVisit.getVisitPhotoList();
        for (VisitPhoto photo : photoList) {
            fileService.deleteFile(photo.getFile().getFileId());
            visitPhotoRepository.delete(photo);
        }

        inspectionVisitRepository.delete(inspectionVisit);

        return new GenericResponse("Deleted inspectionVisit and all related photos successfully.");
    }

    public Object deleteVisitPhoto(UUID id, UUID visitPhotoId) {
        VisitPhoto visitPhoto = visitPhotoRepository.findById(visitPhotoId).orElseThrow(() -> new SKException("Work Order Photo not found.", HttpStatus.NOT_FOUND));
        File file = visitPhoto.getFile();

        visitPhotoRepository.delete(visitPhoto);
        fileService.deleteFile(file.getFileId());
        return GenericResponse.builder().message("File deleted successful.").build();

    }

    public Object viewVisitPhoto(UUID id) {
        InspectionVisit inspectionVisit = inspectionVisitRepository.findById(id).orElseThrow(()->new SKException("Invalid inspectionVisitId", HttpStatus.NOT_FOUND));
        List<VisitPhoto> workOrderFileList = inspectionVisit.getVisitPhotoList();
        return workOrderFileList.stream().map(f -> fileService.getFileResponse(f.getVisitPhotoId(), f.getFile())).toList();
    }

    public Object saveVisitPhoto(UUID id, MultipartFile request) {
        InspectionVisit inspectionVisit = inspectionVisitRepository.findById(id).orElseThrow(()->new SKException("Invalid inspectionVisitId", HttpStatus.NOT_FOUND));
        try {
            File file = fileService.getSavedFile(request);

            VisitPhoto visitPhoto = new VisitPhoto();
            visitPhoto.setInspectionVisit(inspectionVisit);
            visitPhoto.setFile(file);
            visitPhotoRepository.save(visitPhoto);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return GenericResponse.builder().message("File upload successful.").build();
    }
}
