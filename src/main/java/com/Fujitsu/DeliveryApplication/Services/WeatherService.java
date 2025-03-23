package com.Fujitsu.DeliveryApplication.Services;

import com.Fujitsu.DeliveryApplication.Entities.Station;
import com.Fujitsu.DeliveryApplication.Entities.Weather;
import com.Fujitsu.DeliveryApplication.Enums.VehicleType;
import com.Fujitsu.DeliveryApplication.Exceptions.InvalidCityException;
import com.Fujitsu.DeliveryApplication.Exceptions.InvalidDatetimeFormatException;
import com.Fujitsu.DeliveryApplication.Exceptions.InvalidVehicleTypeException;
import com.Fujitsu.DeliveryApplication.Exceptions.WeatherNotFoundException;
import com.Fujitsu.DeliveryApplication.Repositories.StationRepository;
import com.Fujitsu.DeliveryApplication.Repositories.WeatherRepository;
import com.Fujitsu.DeliveryApplication.Utils.CityStationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final BaseFeeService baseFeeService;
    private final ExtraFeeService extraFeeService;
    private final StationRepository stationRepository;
    private final WeatherRepository weatherRepository;

    public Weather getLatestWeather(String city) {
        Station station = getStation(city);
        return weatherRepository.findFirstByStationOrderByObservationTimeDesc(station).orElse(null);
    }

    /**
     * Calculate the delivery fee based on the city, vehicle type, and datetime
     * Sums the base fee and extra fee to get the total fee
     * @return the total fee
     */
    public Object calculateWeatherFee(String city, String vehicleType, String datetime) {
        String formattedVehicleType = vehicleType.substring(0, 1).toUpperCase() + vehicleType.substring(1).toLowerCase();

        VehicleType vehicleTypeEnum;
        try {
            vehicleTypeEnum = VehicleType.valueOf(formattedVehicleType);
        } catch (IllegalArgumentException e) {
            throw new InvalidVehicleTypeException("Invalid vehicle type: " + vehicleType + ". Expected types: " + Arrays.toString(VehicleType.values()));
        }

        double baseFee = baseFeeService.getBaseFee(city, vehicleTypeEnum);

        Weather weather;

        if (datetime == null || datetime.isEmpty()) {
            weather = getLatestWeather(city);
            if (weather == null) {
                throw new WeatherNotFoundException("No weather data found for city: " + city);
            }
        } else {
            weather = getWeather(city, datetime);
            if (weather == null) {
                throw new WeatherNotFoundException("No weather data found for city: " + city + " and datetime: " + datetime);
            }
        }

        Object extraFee = extraFeeService.getExtraFee(weather, vehicleTypeEnum);
        return (extraFee instanceof Double) ? baseFee + (double) extraFee : extraFee;
    }

    /**
     * Get the weather data for the given city and datetime
     */
    public Weather getWeather(String city, String datetime) {
        Station station = getStation(city);

        Timestamp timestamp;
        try {
            timestamp = Timestamp.valueOf(datetime);
        } catch (Exception e) {
            throw new InvalidDatetimeFormatException("Invalid datetime format. Expected format: yyyy-MM-dd HH:mm:ss");
        }

        return weatherRepository.findLastWeatherBeforeOrAt(station, timestamp).orElse(null);
    }

    /**
     * Get the station for the given city from the CityStationMapper
     */
    public Station getStation(String city) {
        return stationRepository.getByStationName(CityStationMapper.getStationByCity(city))
                .orElseThrow(() -> new InvalidCityException("Invalid city: " + city));
    }
}
