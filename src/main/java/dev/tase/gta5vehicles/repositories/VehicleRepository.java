package dev.tase.gta5vehicles.repositories;

import dev.tase.gta5vehicles.entities.Vehicle;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository for Vehicle entity to handle complex queries
 * that go beyond the basic active record pattern.
 */
@ApplicationScoped
public class VehicleRepository implements PanacheRepository<Vehicle> {
    
    /**
     * Find vehicles by partial name match (case insensitive)
     */
    public List<Vehicle> findByNameContaining(String nameFragment) {
        return list("LOWER(name) LIKE LOWER(?1)", "%" + nameFragment + "%");
    }
    
    /**
     * Find vehicles with top speed greater than the specified value
     */
    public List<Vehicle> findByTopSpeedGreaterThan(Double minTopSpeed) {
        return list("topSpeed > ?1", minTopSpeed);
    }
    
    /**
     * Find vehicles by multiple criteria
     */
    public List<Vehicle> findByMultipleCriteria(String vehicleClass, Integer minSeats, BigDecimal maxPrice) {
        StringBuilder query = new StringBuilder();
        boolean hasCondition = false;
        
        if (vehicleClass != null) {
            query.append("vehicleClass = ?1");
            hasCondition = true;
        }
        
        if (minSeats != null) {
            if (hasCondition) {
                query.append(" AND ");
            }
            query.append("seats >= ?2");
            hasCondition = true;
        }
        
        if (maxPrice != null) {
            if (hasCondition) {
                query.append(" AND ");
            }
            query.append("price <= ?3");
        }
        
        return list(query.toString(), vehicleClass, minSeats, maxPrice);
    }
    
    /**
     * Find special vehicles with specific modifications
     */
    public List<Vehicle> findSpecialVehiclesWithModification(String modification) {
        return list("isSpecialVehicle = true AND modifications LIKE ?1", "%" + modification + "%");
    }
}

