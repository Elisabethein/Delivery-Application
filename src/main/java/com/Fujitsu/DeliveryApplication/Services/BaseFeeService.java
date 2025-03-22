package com.Fujitsu.DeliveryApplication.Services;

import com.Fujitsu.DeliveryApplication.Entities.BaseFee;
import com.Fujitsu.DeliveryApplication.Enums.City;
import com.Fujitsu.DeliveryApplication.Enums.VehicleType;
import com.Fujitsu.DeliveryApplication.Exceptions.BaseFeeNotFoundException;
import com.Fujitsu.DeliveryApplication.Exceptions.InvalidCityException;
import com.Fujitsu.DeliveryApplication.Exceptions.InvalidVehicleTypeException;
import com.Fujitsu.DeliveryApplication.Repositories.BaseFeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class BaseFeeService {
    private final BaseFeeRepository baseFeeRepository;

    private static final String INVALID_CITY_MSG = "Invalid city: %s. Expected cities: %s";
    private static final String INVALID_VEHICLE_TYPE_MSG = "Invalid vehicle type: %s. Expected types: %s";
    private static final String BASE_FEE_NOT_FOUND_MSG = "No base fee found for city: %s and vehicle type: %s";
    private static final String BASE_FEE_UPDATED_MSG = "Base fee updated successfully for city: %s and vehicle type: %s";
    private static final String BASE_FEE_DELETED_MSG = "Base fee deleted successfully for city: %s and vehicle type: %s";


    public double getBaseFee(String city, VehicleType vehicleType) {
        City cityEnum = parseCity(city);

        return baseFeeRepository.findByCityAndVehicleType(cityEnum, vehicleType)
                .map(BaseFee::getFee)
                .orElseThrow(() -> new BaseFeeNotFoundException(String.format(BASE_FEE_NOT_FOUND_MSG, cityEnum, vehicleType)));
    }

    public Object getAllBaseFees() {
        return baseFeeRepository.findAll();
    }

    public String updateBaseFee(String city, String vehicleType, double fee) {
        City cityEnum = parseCity(city);
        VehicleType vehicleTypeEnum = parseVehicleType(vehicleType);

        BaseFee baseFee = baseFeeRepository.findByCityAndVehicleType(cityEnum, vehicleTypeEnum)
                .orElseThrow(() -> new BaseFeeNotFoundException(String.format(BASE_FEE_NOT_FOUND_MSG, cityEnum, vehicleTypeEnum)));

        baseFee.setFee(fee);
        baseFeeRepository.save(baseFee);

        return String.format(BASE_FEE_UPDATED_MSG, cityEnum, vehicleTypeEnum);
    }

    public String deleteBaseFee(String city, String vehicleType) {
        City cityEnum = parseCity(city);
        VehicleType vehicleTypeEnum = parseVehicleType(vehicleType);

        BaseFee baseFee = baseFeeRepository.findByCityAndVehicleType(cityEnum, vehicleTypeEnum)
                .orElseThrow(() -> new BaseFeeNotFoundException(String.format(BASE_FEE_NOT_FOUND_MSG, cityEnum, vehicleTypeEnum)));

        baseFeeRepository.delete(baseFee);

        return String.format(BASE_FEE_DELETED_MSG, cityEnum, vehicleTypeEnum);
    }

    private City parseCity(String city) {
        String formattedCity = capitalize(city);
        try {
            return City.valueOf(formattedCity);
        } catch (IllegalArgumentException e) {
            throw new InvalidCityException(String.format(INVALID_CITY_MSG, city, Arrays.toString(City.values())));
        }
    }

    private VehicleType parseVehicleType(String vehicleType) {
        String formattedVehicleType = capitalize(vehicleType);
        try {
            return VehicleType.valueOf(formattedVehicleType);
        } catch (IllegalArgumentException e) {
            throw new InvalidVehicleTypeException(String.format(INVALID_VEHICLE_TYPE_MSG, vehicleType, Arrays.toString(VehicleType.values())));
        }
    }
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("Input string cannot be null or empty");
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
