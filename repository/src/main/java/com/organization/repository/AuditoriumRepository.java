package com.organization.repository;

import com.organization.entity.Auditorium;
import com.organization.entity.City;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface AuditoriumRepository extends CrudRepository<Auditorium, Long> {
  List<Auditorium> findAllByTheatreId(Long id);

  Optional<Auditorium> findById(Long id);

  Optional<Auditorium> findByNameIgnoreCaseAndTheatreId(String name, Long theatreId);

  List<City> findByNameIgnoreCase(String name);

  @Transactional
  void deleteById(Long id);

}
