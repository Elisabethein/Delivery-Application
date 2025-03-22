package com.Fujitsu.DeliveryApplication.Repositories;

import com.Fujitsu.DeliveryApplication.Entities.ExtraFee;
import com.Fujitsu.DeliveryApplication.Entities.ExtraFeeVehicleMapping;
import com.Fujitsu.DeliveryApplication.Entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ExtraFeeVehicleMappingRepository extends JpaRepository<ExtraFeeVehicleMapping, UUID> {
    Optional<ExtraFeeVehicleMapping> findByExtraFeeAndVehicle(ExtraFee rule, Vehicle vehicle);

    void deleteByExtraFee(ExtraFee extraFee);
}
