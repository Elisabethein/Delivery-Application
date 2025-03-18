package com.Fujitsu.DeliveryApplication.Repositories;

import com.Fujitsu.DeliveryApplication.Entities.Station;
import com.Fujitsu.DeliveryApplication.Entities.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

public interface WeatherRepository extends JpaRepository<Weather, UUID> {
    @Query("SELECT w FROM Weather w WHERE w.station = :station ORDER BY w.observationTime DESC")
    Optional<Weather> findFirstByStationOrderByTimestampDesc(Station station);

    @Query("SELECT w FROM Weather w WHERE w.station = :station AND w.observationTime <= :timestamp ORDER BY w.observationTime DESC")
    Optional<Weather> findLastWeatherBeforeOrAt(
            @Param("station") Station station,
            @Param("timestamp") Timestamp timestamp
    );
}
