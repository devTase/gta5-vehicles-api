package dev.tase.gta5vehicles.consumption;

public class VehicleStats {
    private double weightKg;
    private double acceleration;
    private String driveType;
    private String vehicleClass;

    public VehicleStats(double weightKg, double acceleration, String driveType, String vehicleClass) {
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

    public String getDriveType() {
        return driveType;
    }

    public String getVehicleClass() {
        return this.vehicleClass;
    }
}
