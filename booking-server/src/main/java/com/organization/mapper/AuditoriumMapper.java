package com.organization.mapper;

import com.organization.dto.AuditoriumDto;
import com.organization.entity.Auditorium;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class AuditoriumMapper {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdDate", ignore = true),
            @Mapping(target = "updatedDate", ignore = true)
    })
    public abstract Auditorium toEntity(AuditoriumDto dto);

    public abstract AuditoriumDto toDto(Auditorium auditorium);

    public abstract List<Auditorium> toEntityList(List<AuditoriumDto> auditoriumDtos);

    public abstract List<AuditoriumDto> toDtoList(List<Auditorium> auditorium);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdDate", ignore = true),
            @Mapping(target = "updatedDate", ignore = true)
    })
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    public abstract void mapUpdate(@MappingTarget Auditorium auditorium, AuditoriumDto source);

    @AfterMapping
    public void doAfterMapping(@MappingTarget AuditoriumDto destination, Auditorium source) {
        destination.setTheatreId(source.getTheatre().getId());
    }
}
