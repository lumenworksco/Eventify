package be.ucll.fs.project.controller;

import be.ucll.fs.project.unit.model.City;
import be.ucll.fs.project.service.CityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public ResponseEntity<List<City>> getAllCities() {
        return ResponseEntity.ok(cityService.getAllCities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<City> getCityById(@PathVariable Long id) {
        return ResponseEntity.ok(cityService.getCityById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<City> getCityByName(@PathVariable String name) {
        return ResponseEntity.ok(cityService.getCityByName(name));
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<List<City>> getCitiesByCountry(@PathVariable String country) {
        return ResponseEntity.ok(cityService.getCitiesByCountry(country));
    }

    @GetMapping("/region/{region}")
    public ResponseEntity<List<City>> getCitiesByRegion(@PathVariable String region) {
        return ResponseEntity.ok(cityService.getCitiesByRegion(region));
    }

    @PostMapping
    public ResponseEntity<City> createCity(@Valid @RequestBody City city) {
        City createdCity = cityService.createCity(city);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<City> updateCity(@PathVariable Long id, @Valid @RequestBody City city) {
        City updatedCity = cityService.updateCity(id, city);
        return ResponseEntity.ok(updatedCity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        cityService.deleteCity(id);
        return ResponseEntity.noContent().build();
    }
}
