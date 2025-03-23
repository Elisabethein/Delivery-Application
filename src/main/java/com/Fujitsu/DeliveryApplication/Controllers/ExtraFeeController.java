package com.Fujitsu.DeliveryApplication.Controllers;

import com.Fujitsu.DeliveryApplication.Exceptions.*;
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
        }    catch (InvalidCityException | InvalidVehicleTypeException | ExtraFeeNotFoundException |
                    InvalidExtraFeeRuleException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateExtraFee(@RequestParam("extraFeeType") String extraFeeType,
                                           @RequestParam("fee") double fee) {
        try {
            String response = extraFeeService.updateExtraFee(extraFeeType, fee);
            return ResponseEntity.ok(response);
        }  catch (InvalidCityException | InvalidVehicleTypeException | ExtraFeeNotFoundException |
                    InvalidExtraFeeRuleException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred: " + e.getMessage());
        }
    }
}
