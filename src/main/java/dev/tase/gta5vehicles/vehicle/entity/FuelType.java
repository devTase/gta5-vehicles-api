package dev.tase.gta5vehicles.vehicle.entity;

import java.util.Arrays;

public enum FuelType {
    GASOLINE("Gasoline", 1.0),
    DIESEL("Diesel", 0.85),
    ELECTRIC("Electric", 0.0),
    OTHER("Other", 1.0);

    private final String label;
    private final double fuelTypeMultiplier;

    FuelType(String label, double multiplier) {
        this.label = label;
        this.fuelTypeMultiplier = multiplier;
    }

    public String getLabel() {
        return label;
    }

    public double getFuelTypeMultiplier() {
        return fuelTypeMultiplier;
    }

    public static FuelType from(String label) {
        return Arrays.stream(values())
                .filter(f -> f.label.equalsIgnoreCase(label))
                .findFirst()
                .orElse(FuelType.OTHER);
    }
}
