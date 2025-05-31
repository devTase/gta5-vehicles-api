package dev.tase.gta5vehicles.control;

import dev.tase.gta5vehicles.entities.Vehicle;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class VehicleService {
    @Transactional
    public Vehicle createVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Invalid vehicle data");
        }
        vehicle = Vehicle.getEntityManager().merge(vehicle);
        return vehicle;
    }
}
