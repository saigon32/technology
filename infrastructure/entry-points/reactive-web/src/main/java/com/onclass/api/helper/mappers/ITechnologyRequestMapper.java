package com.onclass.api.helper.mappers;

import com.onclass.api.helper.request.dto.TechnologyRequestDto;
import com.onclass.model.technology.Technology;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ITechnologyRequestMapper {

    TechnologyRequestDto toDto(Technology technology);
    Technology toDomain(TechnologyRequestDto technologyRequestDto);
}
