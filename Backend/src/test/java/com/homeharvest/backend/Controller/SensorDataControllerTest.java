//package com.homeharvest.backend.Controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.homeharvest.backend.Entity.SensorData;
//import com.homeharvest.backend.Entity.User;
//import com.homeharvest.backend.Service.SensorDataService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//
//import org.springframework.http.MediaType;
//
//
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//@WebMvcTest(SensorDataController.class)
//public class SensorDataControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private SensorDataService sensorDataService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private SensorData sensorData1;
//    private SensorData sensorData2;
//
//    @BeforeEach
//    public void setUp() {
//        User user = new User(1L, "John", "Doe", "Address", "123456789", "john.doe@example.com", "password", null, null);
//
//        sensorData1 = new SensorData(1L,user, 25.5, 60.0, 80.0, 150.0, LocalDateTime.now());
//        sensorData2 = new SensorData(2L,user, 25.5, 60.0, 80.0, 150.0, LocalDateTime.now());
//    }
//
//    @Test
//    public void testGetAllSensorData() throws Exception {
//        List<SensorData> sensorDataList = Arrays.asList(sensorData1, sensorData2);
//        given(sensorDataService.getAllSensorData()).willReturn(sensorDataList);
//
//        mockMvc.perform(get("/api/sensor-data"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()").value(sensorDataList.size()))
//                .andExpect(jsonPath("$[0].id").value(sensorData1.getId()))
//                .andExpect(jsonPath("$[1].id").value(sensorData2.getId()));
//
//        verify(sensorDataService, times(1)).getAllSensorData();
//    }
//
//    @Test
//    public void testGetSensorDataById() throws Exception {
//        Long sensorId = 1L;
//        given(sensorDataService.getSensorDataById(sensorId)).willReturn(sensorData1);
//
//        mockMvc.perform(get("/api/sensor-data/{id}", sensorId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(sensorData1.getId()))
//                .andExpect(jsonPath("$.value").value(sensorData1.getValue()));
//
//        verify(sensorDataService, times(1)).getSensorDataById(sensorId);
//    }
//
//    @Test
//    public void testSaveSensorData() throws Exception {
//        given(sensorDataService.saveSensorData(sensorData1)).willReturn(sensorData1);
//
//        mockMvc.perform(post("/api/sensor-data")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(sensorData1)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(sensorData1.getId()))
//                .andExpect(jsonPath("$.value").value(sensorData1.getValue()));
//
//        verify(sensorDataService, times(1)).saveSensorData(sensorData1);
//    }
//
//    @Test
//    public void testUpdateSensorData() throws Exception {
//        Long sensorId = 1L;
//        SensorData updatedSensorData = new SensorData(sensorId, 35.0, LocalDateTime.now(), sensorData1.getUser());
//
//        given(sensorDataService.updateSensorData(sensorId, updatedSensorData)).willReturn(updatedSensorData);
//
//        mockMvc.perform(put("/api/sensor-data/{id}", sensorId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updatedSensorData)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.value").value(updatedSensorData.getValue()));
//
//        verify(sensorDataService, times(1)).updateSensorData(sensorId, updatedSensorData);
//    }
//
//    @Test
//    public void testDeleteSensorData() throws Exception {
//        Long sensorId = 1L;
//        doNothing().when(sensorDataService).deleteSensorData(sensorId);
//
//        mockMvc.perform(delete("/api/sensor-data/{id}", sensorId))
//                .andExpect(status().isOk());
//
//        verify(sensorDataService, times(1)).deleteSensorData(sensorId);
//    }
//}
