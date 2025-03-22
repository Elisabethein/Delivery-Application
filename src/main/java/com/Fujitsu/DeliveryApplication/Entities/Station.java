package com.Fujitsu.DeliveryApplication.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "Station")
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String stationName;
    private String cityName;

    public Station() {
    }

    public Station(String stationName, String cityName) {
        this.stationName = stationName;
        this.cityName = cityName;
    }
}
