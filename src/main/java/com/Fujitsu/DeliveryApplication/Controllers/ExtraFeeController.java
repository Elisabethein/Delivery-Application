package com.Fujitsu.DeliveryApplication.Controllers;

import com.Fujitsu.DeliveryApplication.Exceptions.BaseFeeNotFoundException;
import com.Fujitsu.DeliveryApplication.Exceptions.InvalidCityException;
import com.Fujitsu.DeliveryApplication.Exceptions.InvalidVehicleTypeException;
import com.Fujitsu.DeliveryApplication.Services.ExtraFeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/extra-fee")
@RequiredArgsConstructor
public class ExtraFeeController {
    private final ExtraFeeService extraFeeService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllExtraFees() {
        return ResponseEntity.ok(extraFeeService.getAllExtraFees());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteExtraFee(@RequestParam("extraFeeType") String extraFeeType) {
        try {
            String response = extraFeeService.deleteExtraFee(extraFeeType);
            return ResponseEntity.ok(response);
        }    catch (InvalidCityException | InvalidVehicleTypeException | BaseFeeNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred: " + e.getMessage());
        }
    }
}
