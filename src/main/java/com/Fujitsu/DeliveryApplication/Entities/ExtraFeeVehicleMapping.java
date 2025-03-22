package com.Fujitsu.DeliveryApplication.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "ExtraFeeVehicleMapping")
public class ExtraFeeVehicleMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "extra_fee_id")
    private ExtraFee extraFee;
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    public ExtraFeeVehicleMapping() {
    }

    public ExtraFeeVehicleMapping(ExtraFee extraFee, Vehicle vehicle) {
        this.extraFee = extraFee;
        this.vehicle = vehicle;
    }
}
