package dev.tase.gta5vehicles.consumption;

import dev.tase.gta5vehicles.entities.DriveType;
import dev.tase.gta5vehicles.entities.VehicleClass;

public class FuelConsumptionEstimator {
    public static double estimateFuelConsumption(VehicleStats car) {
        if (!VehicleClass.valueOf(car.getVehicleClass()).getHasConsumptions()) return 0.0;

        double baseConsumption = 6.5;
        double weightFactor = 1.0 + (car.getWeightKg() - 1200) / 1000.0;
        double accelerationPenalty = 1.0 + ((100.0 - car.getAcceleration()) / 100.0);
        double tractionMultiplier = switch (DriveType.valueOf(car.getDriveType())) {
            case DriveType.FRONT_WHEEL_DRIVE -> 1.0;
            case DriveType.REAR_WHEEL_DRIVE -> 1.05;
            case DriveType.ALL_WHEEL_DRIVE -> 1.15;
            case DriveType.OTHER -> 1.5;
        };

        double estimatedConsumption = baseConsumption * weightFactor * accelerationPenalty * tractionMultiplier;

        return Math.round(estimatedConsumption * 10.0) / 10.0;
    }
}
