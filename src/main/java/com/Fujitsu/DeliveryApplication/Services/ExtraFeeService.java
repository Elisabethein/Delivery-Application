package com.Fujitsu.DeliveryApplication.Services;

import com.Fujitsu.DeliveryApplication.Entities.Weather;
import com.Fujitsu.DeliveryApplication.Enums.ExtraFeeRule;
import com.Fujitsu.DeliveryApplication.Enums.VehicleType;
import com.Fujitsu.DeliveryApplication.Utils.WeatherRuleMatcher;
import org.springframework.stereotype.Service;

@Service
public class ExtraFeeService {
    public Object getExtraFee(Weather weather, VehicleType vehicleType) {
        return ExtraFeeRule.calculateExtraFee(vehicleType, weather.getTemperature(), weather.getWindSpeed(), WeatherRuleMatcher.matchWeatherRule(weather.getWeatherPhenomenon()));
    }
}
