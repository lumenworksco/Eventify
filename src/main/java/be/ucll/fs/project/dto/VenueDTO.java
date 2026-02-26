package be.ucll.fs.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class VenueDTO {

    @NotBlank(message = "Venue name is required")
    private String name;

    private String address;

    @Positive(message = "Capacity must be positive")
    private Integer capacity;

    @NotNull(message = "City ID is required")
    private Long cityId;

    public VenueDTO() {
    }

    public VenueDTO(String name, String address, Integer capacity, Long cityId) {
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.cityId = cityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }
}
