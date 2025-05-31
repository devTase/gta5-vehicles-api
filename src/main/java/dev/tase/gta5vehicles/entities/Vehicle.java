package dev.tase.gta5vehicles.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a GTA5 vehicle with detailed specifications.
 */
@Entity
public class Vehicle extends PanacheEntityBase {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    
    @Column(nullable = false)
    public String name;
    
    @Column(nullable = false)
    public String manufacturer;
    
    @Column(name = "vehicle_class", nullable = false)
    public String vehicleClass; // Sports, Super, Muscle, etc.
    
    @Column(nullable = false)
    public Integer seats;
    
    @Column(name = "top_speed", nullable = false)
    public Double topSpeed;
    
    @Column(nullable = false)
    public Double acceleration;
    
    @Column(nullable = false)
    public Double braking;
    
    @Column(nullable = false)
    public Double handling;
    
    @Column(nullable = false)
    public BigDecimal price;
    
    @Column(name = "release_date")
    public LocalDate releaseDate;
    
    @Column(name = "is_special_vehicle")
    public Boolean isSpecialVehicle;
    
    @ElementCollection(fetch = FetchType.EAGER)
    public List<String> modifications = new ArrayList<>();
    
    // Default constructor required by JPA
    public Vehicle() {}
    
    // Convenience constructor for creating a new vehicle
    public Vehicle(String name, String manufacturer, String vehicleClass, 
                  Integer seats, Double topSpeed, Double acceleration,
                  Double braking, Double handling, BigDecimal price, 
                  LocalDate releaseDate, Boolean isSpecialVehicle,
                  List<String> modifications) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.vehicleClass = vehicleClass;
        this.seats = seats;
        this.topSpeed = topSpeed;
        this.acceleration = acceleration;
        this.braking = braking;
        this.handling = handling;
        this.price = price;
        this.releaseDate = releaseDate;
        this.isSpecialVehicle = isSpecialVehicle;
        this.modifications = modifications;
    }
    
    // Custom finder methods can be added here for simple queries
    public static List<Vehicle> findByManufacturer(String manufacturer) {
        return list("manufacturer", manufacturer);
    }
    
    public static List<Vehicle> findByVehicleClass(String vehicleClass) {
        return list("vehicleClass", vehicleClass);
    }
    
    public static List<Vehicle> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return list("price >= ?1 and price <= ?2", minPrice, maxPrice);
    }
}

