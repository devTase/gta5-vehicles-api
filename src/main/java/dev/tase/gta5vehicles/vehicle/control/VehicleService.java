package dev.tase.gta5vehicles.vehicle.control;

import dev.tase.gta5vehicles.vehicle.entity.Vehicle;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.Collection;

@ApplicationScoped
public class VehicleService {
    @Transactional
    public void createVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Invalid vehicle data");
        }
        Collection<Vehicle> v = Vehicle.findByName(vehicle.getName());
        if(v.isEmpty()) {
            Vehicle.getEntityManager().merge(vehicle);
        }
    }
}
