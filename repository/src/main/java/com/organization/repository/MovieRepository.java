package com.organization.repository;

import com.organization.entity.Movie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends CrudRepository<Movie, Long> {
  List<Movie> findAll();

  Optional<Movie> findById(Long id);

  List<Movie> findByNameIgnoreCase(String name);

  @Transactional
  void deleteById(Long id);

}
