package com.Fujitsu.DeliveryApplication.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
