package dev.tase.gta5vehicles.entities;

import java.util.Arrays;

public enum DriveType {
    FRONT_WHEEL_DRIVE("Forward-wheel drive"),
    REAR_WHEEL_DRIVE("Rear-Wheel Drive"),
    ALL_WHEEL_DRIVE("All-Wheel Drive"),
    OTHER("N/A");

    private final String label;

    DriveType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static DriveType from(String label) {

        return Arrays.stream(values())
                .filter(d -> d.label.equalsIgnoreCase(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown drive type: " + label));
    }
}
