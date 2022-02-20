package com.organization.service;

import com.organization.dto.CityDto;
import com.organization.entity.City;
import com.organization.exception.EntityAlreadyExistsException;
import com.organization.exception.ResourceNotFoundException;
import com.organization.mapper.CityMapper;
import com.organization.repository.CityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CityService {
    private final CityRepository repository;
    private final CityMapper mapper;

    @Autowired
    public CityService(CityRepository repository, CityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<CityDto> getCityList() {
        log.debug("Fetching city list");
        List<City> cities = repository.findAll();
        return mapper.toDtoList(cities);
    }

    public CityDto getCityById(Long id) {
        log.debug("Fetching city corresponding to Id: {}", id);
        Optional<City> cityOpt = repository.findById(id);
        if(cityOpt.isPresent()) {
            return mapper.toDto(cityOpt.get());
        }
        throw new ResourceNotFoundException("City not found by provided id");
    }

    public CityDto save(CityDto dto) {
        if (repository.findByNameIgnoreCase(dto.getName()) != null) {
            log.error("City already exists with provided name : {}", dto.getName());
            throw new EntityAlreadyExistsException("City already exists with provided name");
        }
        City entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    public CityDto update(Long cityId, CityDto cityDto) {
        Optional<City> cityOptional = repository.findById(cityId);
        if (!cityOptional.isPresent()) {
            log.error("City not existing with provided id : {} ", cityId);
            throw new ResourceNotFoundException("City not existing with provided cityId");
        }
        City city = cityOptional.get();
        mapper.mapUpdate(city, cityDto);
        city = repository.save(city);
        return mapper.toDto(city);
    }

    public void delete(Long id) {
        Optional<City> cityOptional = repository.findById(id);
        if (!cityOptional.isPresent()) {
            log.error("City not existing with provided id : {} ", id);
            throw new ResourceNotFoundException("City not existing with provided cityId");
        }
        repository.deleteById(id);
    }
}
