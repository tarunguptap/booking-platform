package com.organization.mapper;

import com.organization.dto.AuditoriumDto;
import com.organization.dto.CityDto;
import com.organization.entity.Auditorium;
import com.organization.entity.City;
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
public abstract class CityMapper {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdDate", ignore = true),
            @Mapping(target = "updatedDate", ignore = true)
    })
    public abstract City toEntity(CityDto cityDto);

    public abstract CityDto toDto(City city);

    public abstract List<City> toEntityList(List<CityDto> cityDtos);

    public abstract List<CityDto> toDtoList(List<City> city);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdDate", ignore = true),
            @Mapping(target = "updatedDate", ignore = true)
    })
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    public abstract void mapUpdate(@MappingTarget City target, CityDto source);

}
