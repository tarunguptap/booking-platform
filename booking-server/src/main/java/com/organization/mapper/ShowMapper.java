package com.organization.mapper;

import com.organization.dto.ShowDto;
import com.organization.dto.TheatreDto;
import com.organization.entity.Show;
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
public abstract class ShowMapper {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdDate", ignore = true),
            @Mapping(target = "updatedDate", ignore = true)
    })
    public abstract Show toEntity(ShowDto dto);

    public abstract ShowDto toDto(Show show);

    public abstract List<Show> toEntityList(List<ShowDto> showDtos);

    public abstract List<ShowDto> toDtoList(List<Show> shows);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdDate", ignore = true),
            @Mapping(target = "updatedDate", ignore = true)
    })
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    public abstract void mapUpdate(@MappingTarget Show show, ShowDto source);

    @AfterMapping
    public void doAfterMapping(@MappingTarget ShowDto destination, Show source) {
        destination.setMovieId(source.getMovie().getId());
        destination.setAuditoriumId(source.getAuditorium().getId());
    }
}
