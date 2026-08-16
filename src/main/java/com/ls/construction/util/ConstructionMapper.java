package com.ls.construction.util;

import com.ls.construction.model.entity.Project;
import com.ls.construction.model.entity.Material;
import com.ls.construction.model.entity.LabourDetails;
import com.ls.construction.model.entity.PaymentDetails;
import com.ls.construction.model.entity.Types;
import com.ls.construction.model.request.ProjectRequest;
import com.ls.construction.model.request.MaterialRequest;
import com.ls.construction.model.request.LabourDetailsRequest;
import com.ls.construction.model.request.PaymentDetailsRequest;
import com.ls.construction.model.request.TypesRequest;
import com.ls.construction.model.response.ProjectResponse;
import com.ls.construction.model.response.MaterialResponse;
import com.ls.construction.model.response.LabourDetailsResponse;
import com.ls.construction.model.response.PaymentDetailsResponse;
import com.ls.construction.model.response.TypesResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ConstructionMapper {
    ConstructionMapper INSTANCE = Mappers.getMapper(ConstructionMapper.class);
    
    // Project mappings
    ProjectResponse toResponse(Project project);
    
    @Mapping(target = "audit", ignore = true)
    @Mapping(target = "projectId", ignore = true)
    Project toEntity(ProjectRequest projectRequest);
    
    // Material mappings
    @Mapping(source = "project.projectId", target = "projectId")
    @Mapping(source = "project.name", target = "projectName")
    MaterialResponse toResponse(Material material);
    
    @Mapping(target = "audit", ignore = true)
    @Mapping(target = "materialId", ignore = true)
    @Mapping(target = "project", ignore = true)
    Material toEntity(MaterialRequest materialRequest);
    
    // LabourDetails mappings
    @Mapping(source = "project.projectId", target = "projectId")
    @Mapping(source = "project.name", target = "projectName")
    LabourDetailsResponse toResponse(LabourDetails labourDetails);
    
    @Mapping(target = "audit", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "project", ignore = true)
    LabourDetails toEntity(LabourDetailsRequest labourDetailsRequest);
    
    // PaymentDetails mappings
    @Mapping(source = "project.projectId", target = "projectId")
    @Mapping(source = "project.name", target = "projectName")
    PaymentDetailsResponse toResponse(PaymentDetails paymentDetails);
    
    @Mapping(target = "audit", ignore = true)
    @Mapping(target = "paymentId", ignore = true)
    @Mapping(target = "project", ignore = true)
    PaymentDetails toEntity(PaymentDetailsRequest paymentDetailsRequest);
    
    // Types mappings
    TypesResponse toResponse(Types types);
    
    @Mapping(target = "audit", ignore = true)
    @Mapping(target = "id", ignore = true)
    Types toEntity(TypesRequest typesRequest);
}
