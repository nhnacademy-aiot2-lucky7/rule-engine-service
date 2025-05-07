package com.nhnacademy.ruleengineservice.common.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.ruleengineservice.sensor_data.dto.DataDTO;
import com.nhnacademy.ruleengineservice.sensor_data.service.SensorDataProcessorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SensorDataController.class)
@Import(SensorDataControllerTest.TestConfig.class)
class SensorDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SensorDataProcessorService processorService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public SensorDataProcessorService processorService() {
            return Mockito.mock(SensorDataProcessorService.class);
        }
    }

    @Test
    void testReceiveSensorData() throws Exception {
        DataDTO dataDTO = new DataDTO(
                "gateway1",
                "sensor1",
                "temperature",
                40.0,
                20250505L
        );

        mockMvc.perform(post("/ruleEngine/data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dataDTO)))
                .andExpect(status().isOk());

        verify(processorService, times(1)).process(Mockito.eq(dataDTO));
    }
}
