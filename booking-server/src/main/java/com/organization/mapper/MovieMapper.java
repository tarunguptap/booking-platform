package com.organization.mapper;

import com.organization.dto.MovieDto;
import com.organization.entity.Movie;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class MovieMapper {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdDate", ignore = true),
            @Mapping(target = "updatedDate", ignore = true)
    })
    public abstract Movie toEntity(MovieDto dto);

    public abstract MovieDto toDto(Movie movie);

    public abstract List<Movie> toEntityList(List<MovieDto> movieDtos);

    public abstract List<MovieDto> toDtoList(List<Movie> movies);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdDate", ignore = true),
            @Mapping(target = "updatedDate", ignore = true)
    })
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    public abstract void mapUpdate(@MappingTarget Movie movie, MovieDto source);

}
