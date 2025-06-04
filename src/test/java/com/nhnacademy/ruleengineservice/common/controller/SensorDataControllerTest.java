package com.nhnacademy.ruleengineservice.common.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.ruleengineservice.sensor_data.dto.DataDTO;
import com.nhnacademy.ruleengineservice.sensor_data.service.SensorDataProcessorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SensorDataController.class)
class SensorDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SensorDataProcessorService processorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("DTO 형식대로 잘 받아왔는지 확인")
    void testReceiveSensorData() throws Exception {
        DataDTO dataDTO = new DataDTO(
                1L,
                "sensor-01",
                "temperature",
                40.00,
                20250505L
        );

        mockMvc.perform(post("/rule_engine/data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dataDTO)))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<DataDTO> dataCaptor = ArgumentCaptor.forClass(DataDTO.class);

        verify(processorService).process(dataCaptor.capture());

        DataDTO captorRequest = dataCaptor.getValue();
        assertEquals(1L, captorRequest.getGatewayId());
        assertEquals("sensor-01", captorRequest.getSensorId());
        assertEquals("temperature", captorRequest.getDataType());
        assertEquals(40.00, captorRequest.getValue());
        assertEquals(20250505L, captorRequest.getTimestamp());
    }

    @Test
    @DisplayName("데이터를 잘 받아오지 못한 경우")
    void testReceiveSensorData_BadRequest() throws Exception {
        mockMvc.perform(post("/rule_engine/data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest()
        );
    }
}
