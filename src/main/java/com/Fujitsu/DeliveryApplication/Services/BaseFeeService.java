package com.Fujitsu.DeliveryApplication.Services;

import com.Fujitsu.DeliveryApplication.Enums.BaseFeeRule;
import com.Fujitsu.DeliveryApplication.Enums.VehicleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BaseFeeService {
    public double getBaseFee(String city, VehicleType vehicleType) {
        return BaseFeeRule.getBaseFee(city, vehicleType);
    }
}
