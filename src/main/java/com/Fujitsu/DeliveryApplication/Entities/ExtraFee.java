package com.Fujitsu.DeliveryApplication.Entities;

import com.Fujitsu.DeliveryApplication.Enums.ExtraFeeRuleName;
import com.Fujitsu.DeliveryApplication.Enums.VehicleType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ExtraFee")
public class ExtraFee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ExtraFeeRuleName ruleName;
    private double minValue;
    private double maxValue;
    private double fee;
    private String errorMessage;
}
