package be.ucll.fs.project.service;

import be.ucll.fs.project.unit.model.City;
import be.ucll.fs.project.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CityService {

    private final CityRepository cityRepository;

    @Autowired
    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    public City getCityById(Long id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("City not found with id: " + id));
    }

    public City getCityByName(String name) {
        return cityRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("City not found with name: " + name));
    }

    public List<City> getCitiesByCountry(String country) {
        return cityRepository.findByCountry(country);
    }

    public List<City> getCitiesByRegion(String region) {
        return cityRepository.findByRegion(region);
    }

    public City createCity(City city) {
        if (cityRepository.existsByName(city.getName())) {
            throw new IllegalArgumentException("City with name " + city.getName() + " already exists");
        }
        return cityRepository.save(city);
    }

    public City updateCity(Long id, City cityDetails) {
        City city = getCityById(id);
        
        if (!city.getName().equals(cityDetails.getName()) && 
            cityRepository.existsByName(cityDetails.getName())) {
            throw new IllegalArgumentException("City with name " + cityDetails.getName() + " already exists");
        }
        
        city.setName(cityDetails.getName());
        city.setRegion(cityDetails.getRegion());
        city.setCountry(cityDetails.getCountry());
        
        return cityRepository.save(city);
    }

    public void deleteCity(Long id) {
        City city = getCityById(id);
        cityRepository.delete(city);
    }
}
