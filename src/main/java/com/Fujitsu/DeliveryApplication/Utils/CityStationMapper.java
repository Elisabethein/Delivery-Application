package com.Fujitsu.DeliveryApplication.Utils;

import java.util.Map;

public class CityStationMapper {

    private static final Map<String, String> STATION_MAPPINGS = Map.of(
            "Tallinn-Harku", "Tallinn",
            "Tartu-Tõravere", "Tartu",
            "Pärnu", "Pärnu"
    );

    public static Map<String, String> getStationMappings() {
        return STATION_MAPPINGS;
    }

    public static String getStationByCity(String city) {
        return STATION_MAPPINGS.entrySet().stream()
                .filter(entry -> entry.getValue().equals(city))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

}
