package com.Fujitsu.DeliveryApplication.Enums;

import lombok.Getter;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public enum ExtraFeeRule {
    TEMPERATURE_LOW(Double.MIN_VALUE, -10, 1.0, EnumSet.of(VehicleType.SCOOTER, VehicleType.BIKE)),
    TEMPERATURE_BETWEEN(-10, 0, 0.5, EnumSet.of(VehicleType.SCOOTER, VehicleType.BIKE)),

    WIND_BETWEEN(10, 20, 0.5, EnumSet.of(VehicleType.BIKE)),
    WIND_HIGH(20, Double.MAX_VALUE, 0.0, EnumSet.of(VehicleType.BIKE), ExtraFeeRule.FORBIDDEN_MESSAGE),

    WEATHER_SNOW(Double.MIN_VALUE, Double.MAX_VALUE, 1.0, EnumSet.of(VehicleType.SCOOTER, VehicleType.BIKE)),
    WEATHER_SLEET(Double.MIN_VALUE, Double.MAX_VALUE, 1.0, EnumSet.of(VehicleType.SCOOTER, VehicleType.BIKE)),
    WEATHER_RAIN(Double.MIN_VALUE, Double.MAX_VALUE, 0.5, EnumSet.of(VehicleType.SCOOTER, VehicleType.BIKE)),
    WEATHER_GLAZE(Double.MIN_VALUE, Double.MAX_VALUE, 0.0, EnumSet.of(VehicleType.SCOOTER, VehicleType.BIKE), ExtraFeeRule.FORBIDDEN_MESSAGE),
    WEATHER_HAIL(Double.MIN_VALUE, Double.MAX_VALUE, 0.0, EnumSet.of(VehicleType.SCOOTER, VehicleType.BIKE), ExtraFeeRule.FORBIDDEN_MESSAGE),
    WEATHER_THUNDER(Double.MIN_VALUE, Double.MAX_VALUE, 0.0, EnumSet.of(VehicleType.SCOOTER, VehicleType.BIKE), ExtraFeeRule.FORBIDDEN_MESSAGE);

    private static final String FORBIDDEN_MESSAGE = "Usage of selected vehicle type is forbidden";
    private final double min;
    private final double max;
    private final double fee;
    private final Set<VehicleType> applicableVehicles;
    private final String errorMessage;

    ExtraFeeRule(double min, double max, double fee, Set<VehicleType> applicableVehicles) {
        this.min = min;
        this.max = max;
        this.fee = fee;
        this.applicableVehicles = applicableVehicles;
        this.errorMessage = null;
    }

    ExtraFeeRule(double min, double max, double fee, Set<VehicleType> applicableVehicles, String errorMessage) {
        this.min = min;
        this.max = max;
        this.fee = fee;
        this.applicableVehicles = applicableVehicles;
        this.errorMessage = errorMessage;
    }

    public static Object calculateExtraFee(VehicleType vehicleType, double temperature, double windSpeed, List<ExtraFeeRule> weatherConditions) {
        double totalFee = 0.0;

        for (ExtraFeeRule rule : values()) {
            if (!rule.applicableVehicles.contains(vehicleType)) {
                continue;
            }

            boolean conditionMatches =
                    (rule == TEMPERATURE_LOW || rule == TEMPERATURE_BETWEEN) && temperature >= rule.min && temperature < rule.max ||
                            (rule == WIND_BETWEEN || rule == WIND_HIGH) && windSpeed >= rule.min && windSpeed < rule.max ||
                            weatherConditions.contains(rule);

            if (conditionMatches) {
                if (rule.errorMessage != null) {
                    return rule.errorMessage;
                }
                totalFee += rule.fee;
            }
        }

        return totalFee;
    }

}
