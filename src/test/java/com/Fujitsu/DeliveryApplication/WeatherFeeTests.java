package com.Fujitsu.DeliveryApplication;

import com.Fujitsu.DeliveryApplication.Entities.Station;
import com.Fujitsu.DeliveryApplication.Entities.Weather;
import com.Fujitsu.DeliveryApplication.Repositories.StationRepository;
import com.Fujitsu.DeliveryApplication.Repositories.WeatherRepository;
import com.Fujitsu.DeliveryApplication.Services.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class WeatherFeeTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private StationRepository stationRepository;

    private LocalDateTime testTime;

    @BeforeEach
    void setUp() {
        weatherRepository.deleteAll();

        testTime = LocalDateTime.now();

        Station station = stationRepository.getByStationName("Tallinn-Harku").orElse(null);
        Station station2 = stationRepository.getByStationName("Tartu-Tõravere").orElse(null);

        weatherRepository.save(Weather.builder()
                .station(station)
                .observationTime(Timestamp.valueOf(testTime))
                .WMO("12345")
                .temperature(-5.0)
                .windSpeed(20.0)
                .weatherPhenomenon("snowy and light rain showers")
                .build());
        weatherRepository.save(Weather.builder()
                .station(station2)
                .observationTime(Timestamp.valueOf(testTime))
                .WMO("12345")
                .temperature(5.0)
                .windSpeed(5.0)
                .weatherPhenomenon("sunny")
                .build());
    }

    @Test
    void testCalculateDeliveryFeeForDifferentVehicles() throws Exception {
        for (String vehicle : new String[]{"Car", "Bike", "Scooter"}) {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/delivery-fee")
                            .param("city", "Tartu")
                            .param("vehicleType", vehicle))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
        }
    }

    @Test
    void testCalculateDeliveryFeeMissingParameters() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/delivery-fee")
                        .param("city", "Tallinn"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/delivery-fee")
                        .param("vehicleType", "Car"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCalculateDeliveryFeeIncorrectCity() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/delivery-fee")
                .param("city", "Tallinnn")
                .param("vehicleType", "Car"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCalculateDeliveryFeeIncorrectVehicleType() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/delivery-fee")
                .param("city", "Tallinn")
                .param("vehicleType", "Carr"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCalculateDeliveryFeeDatetimeCar() throws Exception {
        String testDateTime = testTime.plusHours(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/delivery-fee")
                .param("city", "Tallinn")
                .param("vehicleType", "Car")
                .param("datetime", testDateTime))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string("4.0"));
    }

    @Test
    void testCalculateDeliveryFeeDatetimeScooter() throws Exception {
        String testDateTime = testTime.plusHours(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/delivery-fee")
                        .param("city", "Tallinn")
                        .param("vehicleType", "Scooter")
                        .param("datetime", testDateTime))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string("5.5"));
    }

    @Test
    void testCalculateDeliveryFeeDatetimeBike() throws Exception {
        String testDateTime = testTime.plusHours(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/delivery-fee")
                        .param("city", "Tallinn")
                        .param("vehicleType", "Bike")
                        .param("datetime", testDateTime))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Usage of selected vehicle type is forbidden"));
    }

    @Test
    void testCalculateDeliveryFeeIncorrectDatetime() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/delivery-fee")
                .param("city", "Tallinn")
                .param("vehicleType", "Car")
                .param("datetime", "2022-01-01 00:00:00"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCalculateDeliveryFeeInvalidDateFormat() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/delivery-fee")
                        .param("city", "Tallinn")
                        .param("vehicleType", "Car")
                        .param("datetime", "invalid-date-format"))
                .andExpect(status().isBadRequest());
    }
}
