package com.Fujitsu.DeliveryApplication;

import com.Fujitsu.DeliveryApplication.Entities.ExtraFee;
import com.Fujitsu.DeliveryApplication.Enums.ExtraFeeRuleName;
import com.Fujitsu.DeliveryApplication.Repositories.ExtraFeeRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ExtraFeeTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExtraFeeRepository extraFeeRepository;

    @Test
    void testGetAllExtraFees() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/extra-fee/all"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()")
                        .value(extraFeeRepository.findAll().size()));
    }

    @Test
    void testDeleteAllExtraFees() throws Exception {
        for (ExtraFeeRuleName rule : ExtraFeeRuleName.values()) {
            assertTrue(extraFeeRepository.findByRuleName(rule).isPresent(), "Expected rule " + rule + " to exist before deletion");

            mockMvc.perform(MockMvcRequestBuilders.delete("/api/extra-fee/delete")
                            .param("extraFeeType", rule.name()))
                    .andExpect(status().isOk());

            assertFalse(extraFeeRepository.findByRuleName(rule).isPresent(), "Expected rule " + rule + " to be deleted but it still exists");
        }
    }


    @Test
    void testDeleteExtraFeeInvalidRule() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/extra-fee/delete")
                        .param("extraFeeType", "INVALID_RULE"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateAllExtraFee() throws Exception {
        for (ExtraFeeRuleName rule : ExtraFeeRuleName.values()) {
            assertFalse(extraFeeRepository.findByRuleName(rule).isEmpty(), "Expected rule " + rule + " to exist before update");

            double newFee = 6.0;

            mockMvc.perform(MockMvcRequestBuilders.put("/api/extra-fee/update")
                            .param("extraFeeType", rule.name())
                            .param("fee", String.valueOf(newFee)))
                    .andExpect(status().isOk());

            ExtraFee updatedRule = extraFeeRepository.findByRuleName(rule)
                    .orElseThrow(() -> new RuntimeException("Rule not found after update"));

            assertEquals(updatedRule.getFee(), newFee, "Expected fee to be updated");
        }
    }

}
