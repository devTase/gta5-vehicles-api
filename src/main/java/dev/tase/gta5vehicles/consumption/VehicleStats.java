package dev.tase.gta5vehicles.consumption;

public class VehicleStats {
    private String category;
    private String driveType;
    private String fuelType;
    private double engineHealth;
    private double weightKg;

    public VehicleStats(String category, String driveType, String fuelType, double engineHealth, double weightKg) {
        this.category = category;
        this.driveType = driveType;
        this.fuelType = fuelType;
        this.engineHealth = engineHealth;
        this.weightKg = weightKg;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDriveType() {
        return driveType;
    }

    public void setDriveType(String driveType) {
        this.driveType = driveType;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public double getEngineHealth() {
        return engineHealth;
    }

    public void setEngineHealth(double engineHealth) {
        this.engineHealth = engineHealth;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(double weightKg) {
        this.weightKg = weightKg;
    }
}
