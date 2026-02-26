package be.ucll.fs.project.dto;

import jakarta.validation.constraints.NotBlank;

public class CityDTO {

    @NotBlank(message = "City name is required")
    private String name;

    private String region;

    @NotBlank(message = "Country is required")
    private String country;

    public CityDTO() {
    }

    public CityDTO(String name, String region, String country) {
        this.name = name;
        this.region = region;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
