package com.onclass.jpa.helper;

import com.onclass.jpa.entity.TechnologyEntity;
import com.onclass.model.technology.Technology;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ITechnologyEntityMapper {
    @Mapping(target = "id", source = "id" )
    Technology toModel(TechnologyEntity entity);
    @Mapping(target = "id", source = "id")
    TechnologyEntity toEntity(Technology technology);
}
