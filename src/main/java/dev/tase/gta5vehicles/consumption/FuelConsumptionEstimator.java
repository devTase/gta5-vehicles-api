package dev.tase.gta5vehicles.consumption;

import dev.tase.gta5vehicles.entities.DriveType;
import dev.tase.gta5vehicles.entities.FuelType;
import dev.tase.gta5vehicles.entities.VehicleCategory;

public class FuelConsumptionEstimator {
    public static double calculateFuelConsumption(VehicleStats car, boolean boostActive, double rpm) {
        VehicleCategory category = VehicleCategory.from(car.getCategory());
        if (!category.getHasConsumptions()) return 0.0;

        DriveType driveType = DriveType.valueOf(car.getDriveType());
        FuelType fuelType = FuelType.valueOf(car.getFuelType()); // assumes car.getFuelType() exists

        double engineHealth = Math.max(0.0, Math.min(100.0, car.getEngineHealth()));
        double engineFactor = Math.max(0.5, engineHealth / 100.0);

        double rpmFactor = Math.max(0.5, rpm); // idle = consumo baixo

        double tractionMultiplier = driveType.getTractionMultiplier();

        double boostMultiplier = boostActive ? 1.2 : 1.0;

        double weightKg = Math.max(500.0, car.getWeightKg()); // sanity check
        double weightMultiplier = 1.0 + (weightKg - 1000.0) / 2000.0; // mais peso = mais consumo

        double fuelTypeMultiplier = fuelType.getFuelTypeMultiplier();

        double result = category.getBaseConsumption()
                * category.getCategoryMultiplier()
                * engineFactor
                * rpmFactor
                * tractionMultiplier
                * boostMultiplier
                * weightMultiplier
                * fuelTypeMultiplier;

        return Math.round(result * 100.0) / 100.0;
    }
}
