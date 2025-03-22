package com.Fujitsu.DeliveryApplication;

import com.Fujitsu.DeliveryApplication.Services.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WeatherFeeTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WeatherService weatherService;

    @Test
    void testCalculateDeliveryFee() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/delivery-fee")
                .param("city", "Tallinn")
                .param("vehicleType", "Car"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

    }

}
