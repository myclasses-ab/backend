package com.classes.Backend.Repository.master;

import com.classes.Backend.Domain.master.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, String> {
    Optional<City> findByNameAndState(String name, String state);
    List<City> findByState(String state);
    List<City> findByIsMetroTrue();
}
