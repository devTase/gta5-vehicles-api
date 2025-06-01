package dev.tase.gta5vehicles.entities;

import java.util.Arrays;

public enum DriveType {
    FRONT_WHEEL_DRIVE("Forward-wheel drive", 1.0),
    REAR_WHEEL_DRIVE("Rear-Wheel Drive", 1.05),
    ALL_WHEEL_DRIVE("All-Wheel Drive", 1.15),
    OTHER("N/A", 1.2);

    private final String label;
    private final double tractionMultiplier;

    DriveType(String label, double tractionMultiplier) {
        this.label = label;
        this.tractionMultiplier = tractionMultiplier;
    }

    public String getLabel() {
        return label;
    }

    public double getTractionMultiplier() {
        return tractionMultiplier;
    }

    public static DriveType from(String label) {
        return Arrays.stream(values())
                .filter(d -> d.label.equalsIgnoreCase(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown drive type: " + label));
    }
}
