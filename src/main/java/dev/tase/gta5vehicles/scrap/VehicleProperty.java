package dev.tase.gta5vehicles.scrap;

public enum VehicleProperty {
    BUY_PRICE(0),
    SELL_PRICE(1),
    SPEED(2),
    ACCELERATION(3),
    BRAKING(4),
    WEIGHT(5),
    DRIVE_TYPE(6),
    SEATS(7);

    private final int index;

    VehicleProperty(int index) {
        this.index = index;
    }
    public int getIndex() {
        return index;
    }
}
