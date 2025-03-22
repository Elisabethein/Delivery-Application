package com.Fujitsu.DeliveryApplication.Services;

import com.Fujitsu.DeliveryApplication.Entities.ExtraFee;
import com.Fujitsu.DeliveryApplication.Entities.ExtraFeeVehicleMapping;
import com.Fujitsu.DeliveryApplication.Entities.Vehicle;
import com.Fujitsu.DeliveryApplication.Entities.Weather;
import com.Fujitsu.DeliveryApplication.Enums.City;
import com.Fujitsu.DeliveryApplication.Enums.ExtraFeeRuleName;
import com.Fujitsu.DeliveryApplication.Enums.VehicleType;
import com.Fujitsu.DeliveryApplication.Exceptions.ExtraFeeNotFoundException;
import com.Fujitsu.DeliveryApplication.Exceptions.VehicleNotFoundException;
import com.Fujitsu.DeliveryApplication.Repositories.ExtraFeeRepository;
import com.Fujitsu.DeliveryApplication.Repositories.ExtraFeeVehicleMappingRepository;
import com.Fujitsu.DeliveryApplication.Repositories.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ExtraFeeService {
    private final VehicleRepository vehicleRepository;
    private final ExtraFeeRepository extraFeeRepository;
    private final ExtraFeeVehicleMappingRepository extraFeeVehicleMappingRepository;

    public Object getExtraFee(Weather weather, VehicleType vehicleType) {
        Vehicle vehicle = vehicleRepository.findByVehicleType(vehicleType)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found for type: " + vehicleType));
        List<ExtraFee> extraFees = extraFeeRepository.findAll();

        Set<ExtraFee> matchedRules = new HashSet<>();

        for (ExtraFee rule : extraFees) {
            if (matchesWeather(rule, weather)) {
                if (matchesVehicle(rule, vehicle)) {
                    matchedRules.add(rule);
                }
            }
        }

        double totalFee = 0.0;
        for (ExtraFee rule : matchedRules) {
            if (rule.getErrorMessage() != null) {
                return rule.getErrorMessage();
            }
            totalFee += rule.getFee();
        }

        return totalFee;
    }

    private boolean matchesVehicle(ExtraFee rule, Vehicle vehicle) {
        Optional<ExtraFeeVehicleMapping> mapping = extraFeeVehicleMappingRepository.findByExtraFeeAndVehicle(rule, vehicle);
        return mapping.isPresent();
    }

    private boolean matchesWeather(ExtraFee rule, Weather weather) {
        double temperature = weather.getTemperature();
        double windSpeed = weather.getWindSpeed();
        String weatherDescription = weather.getWeatherPhenomenon().toLowerCase();

        return switch (rule.getRuleName()) {
            case TEMPERATURE_LOW, TEMPERATURE_BETWEEN ->
                    temperature >= rule.getMinValue() && temperature <= rule.getMaxValue();
            case WIND_BETWEEN, WIND_HIGH -> windSpeed >= rule.getMinValue() && windSpeed <= rule.getMaxValue();
            case WEATHER_SNOW, WEATHER_SLEET, WEATHER_RAIN, WEATHER_GLAZE, WEATHER_HAIL, WEATHER_THUNDER ->
                    weatherDescription.contains(rule.getRuleName().name().split("_")[1].toLowerCase());
        };
    }

    public Object getAllExtraFees() {
        return extraFeeRepository.findAll();
    }

    public String deleteExtraFee(String extraFeeType) {
        ExtraFeeRuleName ruleEnum = ExtraFeeRuleName.valueOf(extraFeeType.toUpperCase());

        ExtraFee extraFee = extraFeeRepository.findByRuleName(ruleEnum)
                .orElseThrow(() -> new ExtraFeeNotFoundException("No extra fee found for rule: " + ruleEnum));

        extraFeeVehicleMappingRepository.deleteByExtraFee(extraFee);
        extraFeeRepository.delete(extraFee);

        return String.format("Extra fee deleted successfully for rule: %s", ruleEnum);
    }
}
