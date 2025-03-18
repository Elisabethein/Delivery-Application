package com.Fujitsu.DeliveryApplication.Services;

import com.Fujitsu.DeliveryApplication.Entities.Station;
import com.Fujitsu.DeliveryApplication.Entities.Weather;
import com.Fujitsu.DeliveryApplication.Enums.VehicleType;
import com.Fujitsu.DeliveryApplication.Repositories.StationRepository;
import com.Fujitsu.DeliveryApplication.Repositories.WeatherRepository;
import com.Fujitsu.DeliveryApplication.Utils.CityStationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final BaseFeeService baseFeeService;
    private final ExtraFeeService extraFeeService;
    private final StationRepository stationRepository;
    private final WeatherRepository weatherRepository;

    public Weather getLatestWeather(String city) {
        Station station = getStation(city);
        return weatherRepository.findFirstByStationOrderByTimestampDesc(station).orElse(null);
    }

    public Object calculateWeatherFee(String city, String vehicleType, String datetime) {
        VehicleType vehicleType1 = VehicleType.valueOf(vehicleType.toUpperCase());
        double baseFee = baseFeeService.getBaseFee(city, vehicleType1);

        Weather weather;

        if (datetime == null || datetime.isEmpty()) {
            weather = getLatestWeather(city);
            if (weather == null) {
                return "No weather data found for city: " + city;
            }
        } else {
            weather = getWeather(city, datetime);
            if (weather == null) {
                return "No weather data found for city: " + city + " at datetime: " + datetime;
            }
        }

        Object extraFee = extraFeeService.getExtraFee(weather, vehicleType1);
        return (extraFee instanceof Double) ? baseFee + (double) extraFee : extraFee;
    }

    public Weather getWeather(String city, String datetime) {
        Station station = getStation(city);

        Timestamp timestamp;
        try {
            timestamp = Timestamp.valueOf(datetime);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid datetime format. Expected format: yyyy-MM-dd HH:mm:ss");
        }

        return weatherRepository.findLastWeatherBeforeOrAt(station, timestamp).orElse(null);
    }

    public Station getStation(String city) {
        return stationRepository.getByStationName(CityStationMapper.getStationByCity(city));
    }
}
