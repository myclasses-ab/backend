package com.classes.Backend.Service.master;

import com.classes.Backend.Domain.master.City;

import java.util.List;
import java.util.Optional;

public interface CityService {
    // ================ CRUD OPERATIONS ===================== //
    City save(City city);
    List<City> saveAll(List<City> cities);
    Optional<City> findById(String identifier);
    List<City> findAll();
    void deleteById(String identifier);
    boolean existsById(String identifier);

    // ================ CUSTOM FINDER METHODS ===================== //
    Optional<City> findByNameAndState(String name, String state);
    List<City> findByState(String state);
    List<City> findByIsMetroTrue();
}
