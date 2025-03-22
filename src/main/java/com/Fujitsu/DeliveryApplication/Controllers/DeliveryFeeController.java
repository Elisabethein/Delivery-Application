package com.Fujitsu.DeliveryApplication.Controllers;

import com.Fujitsu.DeliveryApplication.Exceptions.*;
import com.Fujitsu.DeliveryApplication.Services.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/delivery-fee")
@RequiredArgsConstructor
public class DeliveryFeeController {

    private final WeatherService weatherService;

    @GetMapping
    public ResponseEntity<?> calculateWeatherFee(
            @RequestParam("city") String city,
            @RequestParam("vehicleType") String vehicleType,
            @RequestParam(value = "datetime", required = false) String datetime) {

        try {
            Object response = weatherService.calculateWeatherFee(city, vehicleType, datetime);

            if (response instanceof Double) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response.toString());
            }

        } catch (InvalidCityException | InvalidVehicleTypeException | VehicleNotFoundException | BaseFeeNotFoundException |
                 WeatherNotFoundException | InvalidDatetimeFormatException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred: " + e.getMessage());
        }
    }
}
