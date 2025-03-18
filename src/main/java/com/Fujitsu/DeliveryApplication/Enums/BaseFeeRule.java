package com.Fujitsu.DeliveryApplication.Enums;

import java.util.Map;

public enum BaseFeeRule {
    TALLINN(Map.of(
        VehicleType.CAR, 4.0,
        VehicleType.SCOOTER, 3.5,
        VehicleType.BIKE, 3.0
    )),
    TARTU(Map.of(
        VehicleType.CAR, 3.5,
        VehicleType.SCOOTER, 3.0,
        VehicleType.BIKE, 2.5
    )),
    PÄRNU(Map.of(
        VehicleType.CAR, 3.0,
        VehicleType.SCOOTER, 2.5,
        VehicleType.BIKE, 2.0
    ));

    private final Map<VehicleType, Double> fees;

    BaseFeeRule(Map<VehicleType, Double> fees) {
        this.fees = fees;
    }

    public static double getBaseFee(String station, VehicleType vehicleType) {
        try {
            BaseFeeRule rule = BaseFeeRule.valueOf(station.toUpperCase());
            return rule.fees.getOrDefault(vehicleType, 0.0);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Unknown station: " + station);
        }
    }
}
