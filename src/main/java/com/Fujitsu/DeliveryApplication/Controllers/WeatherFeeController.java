package com.Fujitsu.DeliveryApplication.Controllers;

import com.Fujitsu.DeliveryApplication.Services.BaseFeeService;
import com.Fujitsu.DeliveryApplication.Services.ExtraFeeService;
import com.Fujitsu.DeliveryApplication.Services.WeatherService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/calculateWeatherFee")
@RequiredArgsConstructor
public class WeatherFeeController {

    private final WeatherService weatherService;

    @GetMapping
    public String calculateWeatherFee(@RequestParam("city") String city,
                                      @RequestParam("vehicleType") String vehicleType,
                                      @RequestParam(value = "datetime", required = false) String datetime) {

        try {
            Object response = weatherService.calculateWeatherFee(city, vehicleType, datetime);
            return response instanceof String ? (String) response : String.valueOf(response);
        } catch (Exception e) {
            return "Error occurred: " + e.getMessage();
        }
    }
}
