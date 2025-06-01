package dev.tase.gta5vehicles.entities;

public enum VehicleCategory {
    SPORTS("Sports", true, 7.5, 1.3),
    CLASSIC("Classic", true, 9.0, 1.5),
    SPORTS_CLASSIC("Sports Classic", true, 8.5, 1.2),
    SUPER("Super", true, 10.0, 1.5),
    HELICOPTER("Helicopter", false, 0.0, 0.0),
    SERVICE("Service", false, 0.0, 0.0),
    UTILITY("Utility", false, 7.5, 1.15),
    SUV("SUV", true, 8.0, 1.2),
    MOTORCYCLE("Motorcycle", true, 2.5, 0.8),
    EMERGENCY("Emergency", true, 6.5, 1.1),
    PLANE("Plane", false, 0.0, 0.0),
    MILITARY("Military", false, 0.0, 0.0),
    MUSCLE("Muscle", true, 9.0, 1.4),
    VAN("Van", true, 8.5, 1.3),
    COMPACT("Compact", true, 5.5, 0.9),
    SEDAN("Sedan", true, 6.5, 1.1),
    BOAT("Boat", false, 0.0, 0.0),
    OFF_ROAD("Off-Road", true, 7.0, 1.25),
    OFF_ROAD_SUV("Off-Road, SUV", true, 8.0, 1.2),
    COMMERCIAL("Commercial", true, 9.5, 1.3),
    OPEN_WHEEL("Open Wheel", true, 4.0, 2.8),
    OTHER("N/A", false, 6.5, 1.0); // fallback

    private final String label;
    private final boolean hasConsumptions;
    private final double baseConsumption;
    private final double categoryMultiplier;

    VehicleCategory(String label, boolean hasConsumptions, double baseConsumption, double categoryMultiplier) {
        this.label = label;
        this.hasConsumptions = hasConsumptions;
        this.baseConsumption = baseConsumption;
        this.categoryMultiplier = categoryMultiplier;
    }

    public String getLabel() {
        return label;
    }

    public boolean getHasConsumptions() {
        return hasConsumptions;
    }

    public double getBaseConsumption() {
        return baseConsumption;
    }

    public double getCategoryMultiplier() {
        return categoryMultiplier;
    }

    public static VehicleCategory from(String name) {
        try {
            return VehicleCategory.valueOf(name.toUpperCase().replace("-", "_").replace(" ", "_").replace(",", ""));
        } catch (Exception e) {
            return OTHER;
        }
    }
}
