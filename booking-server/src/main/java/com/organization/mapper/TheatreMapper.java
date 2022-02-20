package com.organization.mapper;

import com.organization.dto.TheatreDto;
import com.organization.entity.Theatre;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class TheatreMapper {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdDate", ignore = true),
            @Mapping(target = "updatedDate", ignore = true)
    })
    public abstract Theatre toEntity(TheatreDto theatreDto);

    public abstract TheatreDto toDto(Theatre theatre);

    public abstract List<Theatre> toEntityList(List<TheatreDto> TheatreDtos);

    public abstract List<TheatreDto> toDtoList(List<Theatre> theatres);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdDate", ignore = true),
            @Mapping(target = "updatedDate", ignore = true)
    })
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    public abstract void mapUpdate(@MappingTarget Theatre target, TheatreDto source);

    @AfterMapping
    public void doAfterMapping(@MappingTarget TheatreDto destination, Theatre source) {
        destination.setCityId(source.getCity().getId());
    }
}
