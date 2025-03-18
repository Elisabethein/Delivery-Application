package com.Fujitsu.DeliveryApplication.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Data
@Getter
@Setter
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
