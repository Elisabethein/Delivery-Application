package com.Fujitsu.DeliveryApplication.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "Weather")
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;
    private String WMO;
    private Double temperature;
    private Double windSpeed;
    private String weatherPhenomenon;
    private Timestamp observationTime;

    public Weather() {
    }

    public Weather(Station station, String WMO, Double temperature, Double windSpeed, String weatherPhenomenon, Timestamp observationTime) {
        this.station = station;
        this.WMO = WMO;
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.weatherPhenomenon = weatherPhenomenon;
        this.observationTime = observationTime;
    }

}
