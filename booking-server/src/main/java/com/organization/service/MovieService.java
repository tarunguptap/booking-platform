package com.organization.service;

import com.organization.dto.MovieDto;
import com.organization.entity.Movie;
import com.organization.exception.EntityAlreadyExistsException;
import com.organization.exception.ResourceNotFoundException;
import com.organization.mapper.MovieMapper;
import com.organization.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MovieService {
    private final MovieRepository repository;
    private final MovieMapper mapper;

    @Autowired
    public MovieService(MovieRepository repository, MovieMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<MovieDto> getMovies() {
        log.debug("Fetching movie list");
        List<Movie> movies = repository.findAll();
        return mapper.toDtoList(movies);
    }

    public MovieDto getMovieById(Long id) {
        log.debug("Fetching movie corresponding to Id: {}", id);
        Optional<Movie> movieOpt = repository.findById(id);
        if(movieOpt.isPresent()) {
            return mapper.toDto(movieOpt.get());
        }
        throw new ResourceNotFoundException("Movie not found by provided id");
    }

    public MovieDto save(MovieDto dto) {
        if (repository.findByNameIgnoreCase(dto.getName()) != null) {
            log.error("Movie already exists with provided name : {}", dto.getName());
            throw new EntityAlreadyExistsException("Movie already exists with provided name");
        }
        Movie entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    public MovieDto update(Long movieId, MovieDto movieDto) {
        Optional<Movie> movieOptional = repository.findById(movieId);
        if (!movieOptional.isPresent()) {
            log.error("Movie not existing with provided id : {} ", movieId);
            throw new ResourceNotFoundException("Movie not existing with provided movieId");
        }
        Movie movie = movieOptional.get();
        mapper.mapUpdate(movie, movieDto);
        movie = repository.save(movie);
        return mapper.toDto(movie);
    }

    public void delete(Long id) {
        Optional<Movie> movieOptional = repository.findById(id);
        if (!movieOptional.isPresent()) {
            log.error("Movie not existing with provided id : {} ", id);
            throw new ResourceNotFoundException("Movie not existing with provided movieId");
        }
        repository.deleteById(id);
    }
}
