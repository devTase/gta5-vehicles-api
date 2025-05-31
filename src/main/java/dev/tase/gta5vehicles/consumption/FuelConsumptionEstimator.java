package dev.tase.gta5vehicles.consumption;

import dev.tase.gta5vehicles.entities.DriveType;
import dev.tase.gta5vehicles.entities.VehicleClass;

public class FuelConsumptionEstimator {

    public static double estimateFuelConsumption(VehicleStats car) {
        if(!car.getVehicleClass().getHasConsumptions()) return 0.0;
        double baseConsumption = 7.0;
        double weightFactor = car.getWeightKg() / 1000.0;
        double accelerationPenalty = 1.0 + ((100.0 - car.getAcceleration()) / 50.0);
        double tractionMultiplier = switch (car.getDriveType()) {
            case DriveType.FRONT_WHEEL_DRIVE -> 1.0;
            case DriveType.REAR_WHEEL_DRIVE -> 1.1;
            case DriveType.ALL_WHEEL_DRIVE-> 1.2;
            case DriveType.OTHER -> 3.0;
        };

        return baseConsumption * weightFactor * accelerationPenalty * tractionMultiplier;
    }
}
