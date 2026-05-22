package com.classes.Backend.Controller.master;

import com.classes.Backend.Domain.master.City;
import com.classes.Backend.Service.master.CityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cities")
public class CityController {

    private final CityServiceImpl CITY_SERVICE_IMPL;

    // ================ CREATE CITY ===================== //
    @PostMapping
    public ResponseEntity<?> saveCity(@RequestBody City city) {
        return new ResponseEntity<>(this.CITY_SERVICE_IMPL.save(city), HttpStatus.CREATED);
    }

    // ================ CREATE ALL CITIES ===================== //
    @PostMapping("/bulk")
    public ResponseEntity<?> saveAllCities(@RequestBody List<City> cities) {
        return new ResponseEntity<>(this.CITY_SERVICE_IMPL.saveAll(cities), HttpStatus.CREATED);
    }

    // ================ GET CITY BY ID ===================== //
    @GetMapping("/{identifier}")
    public ResponseEntity<?> getCityById(@PathVariable String identifier) {
        return new ResponseEntity<>(this.CITY_SERVICE_IMPL.findById(identifier), HttpStatus.OK);
    }

    // ================ GET ALL CITIES ===================== //
    @GetMapping
    public ResponseEntity<?> getAllCities() {
        List<City> allCities = this.CITY_SERVICE_IMPL.findAll();
        return new ResponseEntity<>(allCities, HttpStatus.OK);
    }

    // ================ DELETE CITY BY ID ===================== //
    @DeleteMapping("/{identifier}")
    public ResponseEntity<?> deleteCityById(@PathVariable String identifier) {
        this.CITY_SERVICE_IMPL.deleteById(identifier);
        return new ResponseEntity<>("City deleted successfully", HttpStatus.OK);
    }

    // ================ UPDATE CITY BY ID ===================== //
    @PutMapping("/{identifier}")
    public ResponseEntity<?> updateCityById(@PathVariable String identifier, @RequestBody City city) {
        if (!this.CITY_SERVICE_IMPL.existsById(identifier)) {
            return new ResponseEntity<>("City not found", HttpStatus.NOT_FOUND);
        }
        city.setIdentifier(identifier);
        return new ResponseEntity<>(this.CITY_SERVICE_IMPL.save(city), HttpStatus.OK);
    }

    // ================ FIND BY NAME AND STATE ===================== //
    @GetMapping("/search")
    public ResponseEntity<?> findByNameAndState(@RequestParam String name, @RequestParam String state) {
        return new ResponseEntity<>(this.CITY_SERVICE_IMPL.findByNameAndState(name, state), HttpStatus.OK);
    }

    // ================ FIND BY STATE ===================== //
    @GetMapping("/state/{state}")
    public ResponseEntity<?> findByState(@PathVariable String state) {
        return new ResponseEntity<>(this.CITY_SERVICE_IMPL.findByState(state), HttpStatus.OK);
    }

    // ================ FIND METRO CITIES ===================== //
    @GetMapping("/metro")
    public ResponseEntity<?> findByIsMetroTrue() {
        return new ResponseEntity<>(this.CITY_SERVICE_IMPL.findByIsMetroTrue(), HttpStatus.OK);
    }
}
