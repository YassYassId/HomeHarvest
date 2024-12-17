package com.homeharvest.backend.Controller;

import com.homeharvest.backend.DTO.SensorDataDTO;
import com.homeharvest.backend.Entity.SensorData;
import com.homeharvest.backend.Entity.User;
import com.homeharvest.backend.Repository.UserRepository;
import com.homeharvest.backend.Service.SensorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/sensor-data")
public class SensorDataController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SensorDataService sensorDataService;

    @PostMapping
    public ResponseEntity<?> receiveSensorData(@RequestBody SensorDataDTO data) {
        System.out.println("Received SensorDataDTO: " + data);
        User user = userRepository.findByEmail(data.getEmail());
        if (user == null) {
            return ResponseEntity.status(404).body("Client not found");
        }
        System.out.println("User: " + user.getEmail());

        sensorDataService.saveSensorData(data, user);
        return ResponseEntity.ok("Data received successfully");
    }

    @GetMapping("/{email}")
    public ResponseEntity<List<SensorDataDTO>> getSensorData(@PathVariable String email) {
        List<SensorData> data = sensorDataService.getSensorDataByEmail(email);
        if (data.isEmpty()) {
            return ResponseEntity.status(404).body(Collections.emptyList());
        }

        // Convert to DTOs
        List<SensorDataDTO> dtoList = data.stream().map(sensorData -> {
            SensorDataDTO dto = new SensorDataDTO();
            dto.setEmail(sensorData.getUser().getEmail());
            dto.setTemperature(sensorData.getTemperature());
            dto.setHumidity(sensorData.getHumidity());
            dto.setSoilMoisture(sensorData.getSoilMoisture());
            dto.setWaterConsumption(sensorData.getWaterConsumption());
            dto.setTimestamp(sensorData.getTimestamp());
            return dto;
        }).toList();

        return ResponseEntity.ok(dtoList);
    }

}
