package com.organization.service;

import com.organization.dto.TheatreDto;
import com.organization.entity.City;
import com.organization.entity.Theatre;
import com.organization.exception.EntityAlreadyExistsException;
import com.organization.exception.ResourceNotFoundException;
import com.organization.mapper.TheatreMapper;
import com.organization.repository.CityRepository;
import com.organization.repository.TheatreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class TheatreService {
    private static final int THREAD_COUNT = 10;
    private final TheatreRepository repository;
    private final CityRepository cityRepository;
    private final TheatreMapper mapper;

    @Autowired
    public TheatreService(TheatreRepository repository, CityRepository cityRepository, TheatreMapper mapper) {
        this.repository = repository;
        this.cityRepository = cityRepository;
        this.mapper = mapper;
    }

    public List<TheatreDto> getTheatres(Long cityId) {
        log.debug("Fetching theatre list");
        List<Theatre> theatres = repository.findByCityId(cityId);
        return mapper.toDtoList(theatres);
    }

    public TheatreDto getTheatreById(Long id) {
        log.debug("Fetching theatre corresponding to Id: {}", id);
        Optional<Theatre> theatreOpt = repository.findById(id);
        if (theatreOpt.isPresent()) {
            return mapper.toDto(theatreOpt.get());
        }
        throw new ResourceNotFoundException("Theatre not found by provided id");
    }

    public TheatreDto getTheatreByName(String name, Long cityId) {
        log.debug("Fetching theatre corresponding to Name: {}", name);
        Optional<Theatre> theatreOpt = repository.findByNameIgnoreCaseAndCityId(name, cityId);
        if (theatreOpt.isPresent()) {
            return mapper.toDto(theatreOpt.get());
        }
        throw new ResourceNotFoundException("Theatre not found by provided id");
    }

    public TheatreDto save(Long cityId, TheatreDto dto) {
        Optional<City> city = cityRepository.findById(cityId);
        if (city.isEmpty()) {
            throw new ResourceNotFoundException("City not found", String.format("City not found by Id : %s", cityId));
        }
        Optional<Theatre> theatreOptional = repository.findByNameIgnoreCaseAndCityId(dto.getName(), cityId);
        if (theatreOptional.isPresent()) {
            log.error("Theatre already exists with provided name : {}", dto.getName());
            throw new EntityAlreadyExistsException("Theatre already exists with provided name");
        }
        Theatre entity = mapper.toEntity(dto);
        entity.setCity(city.get());
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    public List<TheatreDto> saveBulk(Long cityId, List<TheatreDto> dtos) {
        final List<CompletableFuture<Void>> completableFutures = Collections.synchronizedList(new ArrayList<>());
        List<TheatreDto> theatreDtos = Collections.synchronizedList(new ArrayList<>());
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        try {
            for (TheatreDto dto : dtos) {
                completableFutures.add(
                        CompletableFuture.runAsync(
                                () -> {
                                    try {
                                        TheatreDto processed = save(cityId, dto);
                                        theatreDtos.add(processed);
                                    } catch (Exception e) {
                                        throw new CompletionException(e);
                                    }
                                },
                                executor));
            }
            CompletableFuture<Void> completable =
                    CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[completableFutures.size()]));
            // Blocks untill all threads complete processing
            completable.get();
        } catch (Exception e) {
            log.error(
                    "Error occurred while processing ",
                    e.getMessage(),
                    e);
            throw new RuntimeException(e);
        } finally {
            executor.shutdownNow();
        }
        return theatreDtos;
    }

    public TheatreDto update(Long theatreId, TheatreDto theatreDto) {
        Optional<Theatre> theatreOptional = repository.findById(theatreId);
        if (!theatreOptional.isPresent()) {
            log.error("Theatre not existing with provided id : {} ", theatreId);
            throw new ResourceNotFoundException("Theatre not existing with provided theatreId");
        }
        Theatre theatre = theatreOptional.get();
        mapper.mapUpdate(theatre, theatreDto);
        theatre = repository.save(theatre);
        return mapper.toDto(theatre);
    }

    public void delete(Long id) {
        Optional<Theatre> theatreOptional = repository.findById(id);
        if (!theatreOptional.isPresent()) {
            log.error("Theatre not existing with provided id : {} ", id);
            throw new ResourceNotFoundException("Theatre not existing with provided theatreId");
        }
        repository.deleteById(id);
    }

    public List<TheatreDto> searchTheater(Long city, String movieName, Date dateTime) {
        List<Theatre> theatres = repository.searchByCityAndNameAndShowTime(city, movieName, dateTime);
        return mapper.toDtoList(theatres);
    }
}
