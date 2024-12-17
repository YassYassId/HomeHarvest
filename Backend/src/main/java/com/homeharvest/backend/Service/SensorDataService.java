package com.homeharvest.backend.Service;

import com.homeharvest.backend.DTO.SensorDataDTO;
import com.homeharvest.backend.Entity.SensorData;
import com.homeharvest.backend.Entity.User;
import com.homeharvest.backend.Repository.SensorDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorDataService {

    @Autowired
    private SensorDataRepository sensorDataRepository;

    public void saveSensorData(SensorDataDTO data, User user) {
        SensorData sensorData = new SensorData();
        sensorData.setUser(user);
        sensorData.setTemperature(data.getTemperature());
        sensorData.setHumidity(data.getHumidity());
        sensorData.setSoilMoisture(data.getSoilMoisture());
        sensorData.setWaterConsumption(data.getWaterConsumption());
        sensorData.setTimestamp(data.getTimestamp());
        System.out.println("Sensor Data: " + sensorData);
        sensorDataRepository.save(sensorData);
    }

    public List<SensorData> getSensorDataByEmail(String email) {
        return sensorDataRepository.findByClientEmail(email);
    }
}
