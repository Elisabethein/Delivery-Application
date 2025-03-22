package com.Fujitsu.DeliveryApplication.Entities;

import com.Fujitsu.DeliveryApplication.Enums.ExtraFeeRuleName;
import com.Fujitsu.DeliveryApplication.Enums.VehicleType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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

    public ExtraFee() {
    }

    public ExtraFee(ExtraFeeRuleName ruleName, double minValue, double maxValue, double fee, String errorMessage) {
        this.ruleName = ruleName;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.fee = fee;
        this.errorMessage = errorMessage;
    }

}
