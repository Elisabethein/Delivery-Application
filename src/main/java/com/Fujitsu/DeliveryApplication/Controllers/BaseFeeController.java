package com.Fujitsu.DeliveryApplication.Controllers;

import com.Fujitsu.DeliveryApplication.Exceptions.*;
import com.Fujitsu.DeliveryApplication.Services.BaseFeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/base-fee")
@RequiredArgsConstructor
public class BaseFeeController {
    private final BaseFeeService baseFeeService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllBaseFees() {
        return ResponseEntity.ok(baseFeeService.getAllBaseFees());
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateBaseFee(@RequestParam("city") String city,
                                           @RequestParam("vehicleType") String vehicleType,
                                           @RequestParam("fee") double fee) {
        try {
            String response = baseFeeService.updateBaseFee(city, vehicleType, fee);
            return ResponseEntity.ok(response);
        }  catch (InvalidCityException | InvalidVehicleTypeException | BaseFeeNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteBaseFee(@RequestParam("city") String city,
                                           @RequestParam("vehicleType") String vehicleType) {
        try {
            String response = baseFeeService.deleteBaseFee(city, vehicleType);
            return ResponseEntity.ok(response);
        }  catch (InvalidCityException | InvalidVehicleTypeException | BaseFeeNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred: " + e.getMessage());
        }
    }

}
