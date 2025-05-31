package dev.tase.gta5vehicles.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a GTA5 vehicle with detailed specifications.
 */
@Entity
@Table(name = "vehicle")
public class Vehicle extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    
    @Column
    public String name;
    
    @Column
    public String manufacturer;
    
    @Column(name = "vehicle_class")
    public String vehicleClass;

    @Column(name = "drive_type")
    public String driveType;
    
    @Column
    public Integer seats;
    
    @Column(name = "top_speed")
    public Double topSpeed;

    @Column
    public Double acceleration;

    @Column
    public Double braking;

    @Column
    public BigDecimal price;

    @Column
    public Double weight;

    @Column
    public Double consumptions;

    // Default constructor required by JPA
    public Vehicle() {}

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public Double getTopSpeed() {
        return topSpeed;
    }

    public void setTopSpeed(Double topSpeed) {
        this.topSpeed = topSpeed;
    }

    public Double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Double acceleration) {
        this.acceleration = acceleration;
    }

    public Double getBraking() {
        return braking;
    }

    public void setBraking(Double braking) {
        this.braking = braking;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getVehicleClass() {
        return vehicleClass;
    }

    public void setVehicleClass(String vehicleClass) {
        this.vehicleClass = vehicleClass;
    }

    public String getDriveType() {
        return driveType;
    }

    public void setDriveType(String driveType) {
        this.driveType = driveType;
    }

    public Double getConsumptions() {
        return consumptions;
    }

    public void setConsumptions(Double consumptions) {
        this.consumptions = consumptions;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", vehicleClass=" + vehicleClass +
                ", driveType=" + driveType +
                ", seats=" + seats +
                ", topSpeed=" + topSpeed +
                ", acceleration=" + acceleration +
                ", braking=" + braking +
                ", price=" + price +
                ", weight=" + weight +
                ", consumptions=" + consumptions +
                '}';
    }
}

