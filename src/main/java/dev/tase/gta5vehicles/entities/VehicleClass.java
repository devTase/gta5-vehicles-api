package dev.tase.gta5vehicles.entities;

import java.util.Arrays;

public enum VehicleClass {
    SPORTS("Sports", true),
    CLASSIC("Classic", true),
    SPORTS_CLASSIC("Sports Classic", true),
    SUPER("Super", true),
    HELICOPTER("Helicopter", true),
    SERVICE("Service", false),
    UTILITY("Utility", false),
    SUV("SUV", true),
    MOTORCYCLE("Motorcycle", true),
    EMERGENCY("Emergency", true),
    PLANE("Plane", false),
    MILITARY("Military", false),
    MUSCLE("Muscle", true),
    VAN("Van", true ),
    COMPACT("Compact", true),
    SEDAN("Sedan", true),
    BOAT("Boat", false),
    OFF_ROAD("Off-Road", true),
    OFF_ROAD_SUV("Off-Road, SUV", true),
    COMMERCIAL("Commercial", true),
    OPEN_WHEEL("Open Wheel", true),
    OTHER("N/A", false);

    private String name;
    private Boolean hasConsumptions;

    VehicleClass(String name, Boolean hasConsumptions) {
        this.name = name;
        this.hasConsumptions = hasConsumptions;
    }

    public String getName() {
        return name;
    }

    public Boolean getHasConsumptions() {
        return hasConsumptions;
    }

    public static VehicleClass from(String name) {
        return Arrays.stream(VehicleClass.values())
                .filter(vehicleType -> vehicleType.name.equalsIgnoreCase(name))
                .findFirst().orElse(VehicleClass.OTHER);
    }
}
