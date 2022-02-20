package com.organization.service;

import com.organization.dto.AuditoriumDto;
import com.organization.entity.Auditorium;
import com.organization.entity.City;
import com.organization.entity.Movie;
import com.organization.entity.Theatre;
import com.organization.exception.EntityAlreadyExistsException;
import com.organization.exception.ResourceNotFoundException;
import com.organization.mapper.AuditoriumMapper;
import com.organization.repository.AuditoriumRepository;
import com.organization.repository.MovieRepository;
import com.organization.repository.TheatreRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AuditoriumService {
    private final AuditoriumRepository repository;
    private final TheatreRepository theatreRepository;
    private final MovieRepository movieRepository;
    private final AuditoriumMapper mapper;

    @Autowired
    public AuditoriumService(AuditoriumRepository repository, TheatreRepository theatreRepository,
                             MovieRepository movieRepository, AuditoriumMapper mapper) {
        this.repository = repository;
        this.theatreRepository = theatreRepository;
        this.movieRepository = movieRepository;
        this.mapper = mapper;
    }

    public List<AuditoriumDto> getAuditoriumsByTheatreId(Long theatreId) {
        log.debug("Fetching auditorium list based on theatreId");
        List<Auditorium> auditoriums = repository.findAllByTheatreId(theatreId);
        return mapper.toDtoList(auditoriums);
    }

    public AuditoriumDto getAuditoriumById(Long id) {
        log.debug("Fetching auditorium corresponding to Id: {}", id);
        Optional<Auditorium> auditoriumOpt = repository.findById(id);
        if(auditoriumOpt.isPresent()) {
            return mapper.toDto(auditoriumOpt.get());
        }
        throw new ResourceNotFoundException("Auditorium not found by provided id");
    }

    public AuditoriumDto save(AuditoriumDto dto) {
        Optional<Theatre> theatre = theatreRepository.findById(dto.getTheatreId());
        if (theatre.isEmpty()) {
            throw new ResourceNotFoundException("Theatre not found", String.format("Theatre not found by Id : %s", dto.getTheatreId()));
        }
        if (repository.findByNameIgnoreCaseAndTheatreId(dto.getName(), dto.getTheatreId()) != null) {
            log.error("Auditorium already exists with provided name : {} and theatreId : {}", dto.getName(), dto.getTheatreId());
            throw new EntityAlreadyExistsException("Auditorium already exists with provided name and theatreId");
        }
        Auditorium entity = mapper.toEntity(dto);
        entity.setTheatre(theatre.get());
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    public AuditoriumDto update(Long auditoriumId, AuditoriumDto auditoriumDto) {
        Optional<Auditorium> auditoriumOptional = repository.findById(auditoriumId);
        if (!auditoriumOptional.isPresent()) {
            log.error("Auditorium not existing with provided id : {} ", auditoriumId);
            throw new ResourceNotFoundException("Auditorium not existing with provided auditoriumId");
        }
        Auditorium auditorium = auditoriumOptional.get();
        if(!StringUtils.equals(auditoriumDto.getName(), auditorium.getName())) {
            Optional<Auditorium> optionalAuditorium = repository.findByNameIgnoreCaseAndTheatreId(auditoriumDto.getName(), auditoriumDto.getTheatreId());
            if(optionalAuditorium.isPresent()) {
                log.error("Auditorium already exists with provided name : {} ", auditoriumDto.getName());
                throw new ResourceNotFoundException("Auditorium already existing with provided name");
            }
        }
        mapper.mapUpdate(auditorium, auditoriumDto);
        auditorium = repository.save(auditorium);
        return mapper.toDto(auditorium);
    }

    public void delete(Long id) {
        Optional<Auditorium> auditoriumOptional = repository.findById(id);
        if (!auditoriumOptional.isPresent()) {
            log.error("Auditorium not existing with provided id : {} ", id);
            throw new ResourceNotFoundException("Auditorium not existing with provided auditoriumId");
        }
        repository.deleteById(id);
    }
}
