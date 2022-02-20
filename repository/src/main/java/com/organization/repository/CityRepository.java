package com.organization.repository;

import com.organization.entity.City;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends CrudRepository<City, Long> {
  List<City> findAll();

  Optional<City> findById(Long id);

  Optional<City> findByNameIgnoreCaseAndState(String name, String state);

  List<City> findByNameIgnoreCase(String name);

  @Transactional
  void deleteById(Long id);

}
