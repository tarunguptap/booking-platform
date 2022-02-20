package com.organization.repository;

import com.organization.entity.Theatre;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TheatreRepository extends CrudRepository<Theatre, Long> {
  List<Theatre> findAll();

  List<Theatre> findByCityId(Long cityId);

  Optional<Theatre> findById(Long id);

  Optional<Theatre> findByNameIgnoreCaseAndCityId(String name, Long cityId);

  List<Theatre> findByNameIgnoreCase(String name);

  @Transactional
  void deleteById(Long id);

  @Query(value = "Select t.* from show s join auditorium a on(s.auditorium_id=a.id) join movie m on(s.movie_id=m.id) join theatre t on(t.id = a.theatre_id)\n" +
          " join city c on (c.id = t.city_id) where c.id = :cityId and m.name = :movieName and s.start_time = :showStartDateTime",
          nativeQuery = true)
  List<Theatre> searchByCityAndNameAndShowTime(@Param("cityId") Long cityId, @Param("movieName") String movieName, @Param("showStartDateTime") Date showStartDateTime);

}
