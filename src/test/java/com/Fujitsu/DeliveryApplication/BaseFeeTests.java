package com.Fujitsu.DeliveryApplication;

import com.Fujitsu.DeliveryApplication.Entities.BaseFee;
import com.Fujitsu.DeliveryApplication.Enums.City;
import com.Fujitsu.DeliveryApplication.Enums.VehicleType;
import com.Fujitsu.DeliveryApplication.Repositories.BaseFeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BaseFeeTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BaseFeeRepository baseFeeRepository;

    private final City testCity = City.Tallinn;
    private final VehicleType testVehicleType = VehicleType.Car;

    @BeforeEach
    void setUp() {
        baseFeeRepository.deleteAll();
        double testFee = 4.0;
        baseFeeRepository.save(new BaseFee(testCity, testVehicleType, testFee));
    }

    @Test
    void testGetAllBaseFees() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/base-fee/all"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1));
    }

    @Test
    void testGetAllBaseFeesEmpty() throws Exception {
        baseFeeRepository.deleteAll();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/base-fee/all"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0));
    }

    @Test
    void testUpdateBaseFee() throws Exception {
        double newFee = 6.0;

        mockMvc.perform(MockMvcRequestBuilders.put("/api/base-fee/update")
                .param("city", testCity.name())
                .param("vehicleType", testVehicleType.name())
                .param("fee", String.valueOf(newFee)))
                .andExpect(status().isOk());

        BaseFee updatedBaseFee = baseFeeRepository.findByCityAndVehicleType(testCity, testVehicleType)
                .orElseThrow(() -> new RuntimeException("Base fee not found"));

        assertThat(updatedBaseFee.getFee()).isEqualTo(newFee);
    }

    @Test
    void testUpdateBaseFeeInvalidCity() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/base-fee/update")
                        .param("city", "InvalidCity")
                        .param("vehicleType", testVehicleType.name())
                        .param("fee", "5.0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateBaseFeeInvalidVehicleType() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/base-fee/update")
                        .param("city", testCity.name())
                        .param("vehicleType", "InvalidVehicleType")
                        .param("fee", "5.0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteBaseFee() throws Exception {
        assertThat(baseFeeRepository.findByCityAndVehicleType(testCity, testVehicleType)).isNotEmpty();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/base-fee/delete")
                .param("city", testCity.name())
                .param("vehicleType", testVehicleType.name()))
                .andExpect(status().isOk());

        assertThat(baseFeeRepository.findByCityAndVehicleType(testCity, testVehicleType)).isEmpty();
    }

    @Test
    void testDeleteNonExistentBaseFee() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/base-fee/delete")
                .param("city", "NonExistentCity")
                .param("vehicleType", testVehicleType.name()))
                .andExpect(status().isBadRequest());
    }


}
