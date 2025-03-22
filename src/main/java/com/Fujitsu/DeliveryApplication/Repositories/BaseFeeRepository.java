package com.Fujitsu.DeliveryApplication.Repositories;

import com.Fujitsu.DeliveryApplication.Entities.BaseFee;
import com.Fujitsu.DeliveryApplication.Enums.City;
import com.Fujitsu.DeliveryApplication.Enums.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BaseFeeRepository extends JpaRepository<BaseFee, UUID> {

    Optional<BaseFee> findByCityAndVehicleType(City city, VehicleType vehicleType);
}
