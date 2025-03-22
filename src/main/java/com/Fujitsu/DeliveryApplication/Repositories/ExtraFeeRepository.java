package com.Fujitsu.DeliveryApplication.Repositories;

import com.Fujitsu.DeliveryApplication.Entities.ExtraFee;
import com.Fujitsu.DeliveryApplication.Enums.ExtraFeeRuleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ExtraFeeRepository extends JpaRepository<ExtraFee, UUID> {
    Optional<ExtraFee> findByRuleName(ExtraFeeRuleName ruleEnum);
}
