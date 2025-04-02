package com.onclass.api.helper.mappers;

import com.onclass.api.helper.request.dto.TechnologyResponseDto;
import com.onclass.model.technology.Technology;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ITechnologyResponseMapper {
    @Mapping(target = "id", source = "id")
    TechnologyResponseDto toDto (Technology technology);
    Technology toDomain (TechnologyResponseDto technologyResponseDto);
}
