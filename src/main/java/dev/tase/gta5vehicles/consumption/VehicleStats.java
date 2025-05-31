package dev.tase.gta5vehicles.consumption;

import dev.tase.gta5vehicles.entities.DriveType;
import dev.tase.gta5vehicles.entities.VehicleClass;

public class VehicleStats {
    private double weightKg;
    private double acceleration;
    private DriveType driveType;
    private VehicleClass vehicleClass;

    public VehicleStats(double weightKg, double acceleration, DriveType driveType, VehicleClass vehicleClass) {
        this.weightKg = weightKg;
        this.acceleration = acceleration;
        this.driveType = driveType;
        this.vehicleClass = vehicleClass;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public DriveType getDriveType() {
        return driveType;
    }

    public VehicleClass getVehicleClass() {
        return this.vehicleClass;
    }
}
