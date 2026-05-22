package com.classes.Backend.Service.master;

import com.classes.Backend.Domain.master.City;
import com.classes.Backend.Repository.master.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CityServiceImpl implements CityService {
    private final CityRepository CITY_REPOSITORY;

    // ================ SAVE CITY ===================== //
    @Override
    public City save(City city) {
        return this.CITY_REPOSITORY.save(city);
    }

    // ================ SAVE ALL CITIES ===================== //
    @Override
    public List<City> saveAll(List<City> cities) {
        return this.CITY_REPOSITORY.saveAll(cities);
    }

    // ================ FIND BY ID ===================== //
    @Override
    public Optional<City> findById(String identifier) {
        return this.CITY_REPOSITORY.findById(identifier);
    }

    // ================ FIND ALL ===================== //
    @Override
    public List<City> findAll() {
        return this.CITY_REPOSITORY.findAll();
    }

    // ================ DELETE BY ID ===================== //
    @Override
    public void deleteById(String identifier) {
        if (!this.CITY_REPOSITORY.existsById(identifier)) {
            throw new RuntimeException("City with identifier '" + identifier + "' not found");
        }
        this.CITY_REPOSITORY.deleteById(identifier);
    }

    // ================ EXISTS BY ID ===================== //
    @Override
    public boolean existsById(String identifier) {
        return this.CITY_REPOSITORY.existsById(identifier);
    }

    // ================ FIND BY NAME AND STATE ===================== //
    @Override
    public Optional<City> findByNameAndState(String name, String state) {
        return this.CITY_REPOSITORY.findByNameAndState(name, state);
    }

    // ================ FIND BY STATE ===================== //
    @Override
    public List<City> findByState(String state) {
        return this.CITY_REPOSITORY.findByState(state);
    }

    // ================ FIND BY IS METRO TRUE ===================== //
    @Override
    public List<City> findByIsMetroTrue() {
        return this.CITY_REPOSITORY.findByIsMetroTrue();
    }
}
