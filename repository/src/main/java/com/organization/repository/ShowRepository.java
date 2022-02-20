package com.organization.repository;

import com.organization.entity.Show;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ShowRepository extends CrudRepository<Show, Long> {
  List<Show> findAll();

  @Query(value = "SELECT * FROM Movie m WHERE m.movie_id = :movieId and m.auditorium_id = :auditoriumId and m.end_date > CURRENT_DATE",
          nativeQuery = true)
  List<Show> findByMovieIdAndAuditoriumId(@Param("movieId") Long movieId, @Param("auditoriumId") Long auditoriumId);

  Optional<Show> findById(Long id);

  @Transactional
  void deleteById(Long id);

}
