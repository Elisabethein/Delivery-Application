package com.Fujitsu.DeliveryApplication.Entities;

import com.Fujitsu.DeliveryApplication.Enums.City;
import com.Fujitsu.DeliveryApplication.Enums.VehicleType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "BaseFee")
public class BaseFee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private City city;
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;
    private double fee;

    public BaseFee() {
    }

    public BaseFee(City city, VehicleType vehicleType, double fee) {
        this.city = city;
        this.vehicleType = vehicleType;
        this.fee = fee;
    }
}