package com.organization.service;

import com.organization.dto.ShowDto;
import com.organization.entity.Auditorium;
import com.organization.entity.Movie;
import com.organization.entity.Show;
import com.organization.exception.EntityAlreadyExistsException;
import com.organization.exception.ResourceNotFoundException;
import com.organization.mapper.ShowMapper;
import com.organization.repository.AuditoriumRepository;
import com.organization.repository.MovieRepository;
import com.organization.repository.ShowRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ShowService {
    private final ShowRepository repository;
    private final MovieRepository movieRepository;
    private final AuditoriumRepository auditoriumRepository;
    private final ShowMapper mapper;

    @Autowired
    public ShowService(ShowRepository repository, MovieRepository movieRepository,
                       AuditoriumRepository auditoriumRepository, ShowMapper mapper) {
        this.repository = repository;
        this.movieRepository = movieRepository;
        this.auditoriumRepository = auditoriumRepository;
        this.mapper = mapper;
    }

    public List<ShowDto> getShows(Long movieId, Long audiId) {
        log.debug("Fetching show list");
        List<Show> shows = repository.findByMovieIdAndAuditoriumId(movieId, audiId);
        return mapper.toDtoList(shows);
    }

    public ShowDto save(ShowDto dto) {
        Optional<Movie> movie = movieRepository.findById(dto.getMovieId());
        if (movie.isEmpty()) {
            throw new ResourceNotFoundException("Movie not found", String.format("Movie not found by Id : %s", dto.getMovieId()));
        }

        Optional<Auditorium> auditorium = auditoriumRepository.findById(dto.getAuditoriumId());
        if (auditorium.isEmpty()) {
            throw new ResourceNotFoundException("Auditorium not found", String.format("Auditorium not found by Id : %s", dto.getAuditoriumId()));
        }
        if (repository.findByMovieIdAndAuditoriumId(dto.getMovieId(), dto.getAuditoriumId()) != null) {
            log.error("Show already exists with provided details");
            throw new EntityAlreadyExistsException("Show already exists with provided details");
        }
        Show entity = mapper.toEntity(dto);
        entity.setMovie(movie.get());
        entity.setAuditorium(auditorium.get());
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    public ShowDto update(Long showId, ShowDto showDto) {
        Optional<Show> showOptional = repository.findById(showId);
        if (!showOptional.isPresent()) {
            log.error("Show not existing with provided id : {} ", showId);
            throw new ResourceNotFoundException("Show not existing with provided showId");
        }
        Show show = showOptional.get();
        mapper.mapUpdate(show, showDto);
        show = repository.save(show);
        return mapper.toDto(show);
    }

    public void delete(Long id) {
        Optional<Show> showOptional = repository.findById(id);
        if (!showOptional.isPresent()) {
            log.error("Show not existing with provided id : {} ", id);
            throw new ResourceNotFoundException("Show not existing with provided showId");
        }
        repository.deleteById(id);
    }
}
