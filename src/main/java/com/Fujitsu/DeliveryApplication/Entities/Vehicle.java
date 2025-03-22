package com.Fujitsu.DeliveryApplication.Entities;

import com.Fujitsu.DeliveryApplication.Enums.VehicleType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "Vehicle")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    public Vehicle() {
    }

    public Vehicle(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
}
