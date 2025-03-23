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
    private static final String BASE_FEE_NOT_FOUND_MSG = "No base fee found for city: %s and vehicle type: %s";
    private static final String BASE_FEE_UPDATED_MSG = "Base fee updated successfully for city: %s and vehicle type: %s";
    private static final String BASE_FEE_DELETED_MSG = "Base fee deleted successfully for city: %s and vehicle type: %s";


    /**
     * Get the base fee for the given city and vehicle type
     * If no base fee is found, throw a BaseFeeNotFoundException
     */
    public double getBaseFee(String city, VehicleType vehicleType) {
        City cityEnum = parseEnum(City.class, capitalize(city), InvalidCityException.class);

        return baseFeeRepository.findByCityAndVehicleType(cityEnum, vehicleType)
                .map(BaseFee::getFee)
                .orElseThrow(() -> new BaseFeeNotFoundException(String.format(BASE_FEE_NOT_FOUND_MSG, cityEnum, vehicleType)));
    }

    public Object getAllBaseFees() {
        return baseFeeRepository.findAll();
    }

    /**
     * Update the base fee for the given city and vehicle type
     * If no base fee is found, throw a BaseFeeNotFoundException
     */
    public String updateBaseFee(String city, String vehicleType, double fee) {
        City cityEnum = parseEnum(City.class, capitalize(city), InvalidCityException.class);
        VehicleType vehicleTypeEnum = parseEnum(VehicleType.class, vehicleType, InvalidVehicleTypeException.class);

        BaseFee baseFee = baseFeeRepository.findByCityAndVehicleType(cityEnum, vehicleTypeEnum)
                .orElseThrow(() -> new BaseFeeNotFoundException(String.format(BASE_FEE_NOT_FOUND_MSG, cityEnum, vehicleTypeEnum)));

        baseFee.setFee(fee);
        baseFeeRepository.save(baseFee);

        return String.format(BASE_FEE_UPDATED_MSG, cityEnum, vehicleTypeEnum);
    }

    /**
     * Delete the base fee for the given city and vehicle type
     * If no base fee is found, throw a BaseFeeNotFoundException
     */
    public String deleteBaseFee(String city, String vehicleType) {
        City cityEnum = parseEnum(City.class, capitalize(city), InvalidCityException.class);
        VehicleType vehicleTypeEnum = parseEnum(VehicleType.class, vehicleType, InvalidVehicleTypeException.class);

        BaseFee baseFee = baseFeeRepository.findByCityAndVehicleType(cityEnum, vehicleTypeEnum)
                .orElseThrow(() -> new BaseFeeNotFoundException(String.format(BASE_FEE_NOT_FOUND_MSG, cityEnum, vehicleTypeEnum)));

        baseFeeRepository.delete(baseFee);

        return String.format(BASE_FEE_DELETED_MSG, cityEnum, vehicleTypeEnum);
    }

    private <E extends Enum<E>, X extends RuntimeException> E parseEnum(Class<E> enumType, String value, Class<X> exceptionType) {
        try {
            return Enum.valueOf(enumType, value);
        } catch (IllegalArgumentException e) {
            String message = String.format("Invalid %s: %s. Expected values: %s", enumType.getSimpleName(), value, Arrays.toString(enumType.getEnumConstants()));
            throw createException(exceptionType, message);
        }
    }

    private <X extends RuntimeException> X createException(Class<X> exceptionType, String message) {
        try {
            return exceptionType.getConstructor(String.class).newInstance(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create exception: " + exceptionType.getSimpleName(), e);
        }
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("Input string cannot be null or empty");
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
