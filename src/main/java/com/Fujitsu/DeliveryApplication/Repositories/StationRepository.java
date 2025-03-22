package com.Fujitsu.DeliveryApplication.Repositories;

import com.Fujitsu.DeliveryApplication.Entities.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StationRepository extends JpaRepository<Station, UUID> {
    Optional<Station> getByStationName(String name);
}
