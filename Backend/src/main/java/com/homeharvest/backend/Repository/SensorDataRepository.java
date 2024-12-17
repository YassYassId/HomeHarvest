package com.homeharvest.backend.Repository;

import com.homeharvest.backend.Entity.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SensorDataRepository extends JpaRepository<SensorData, Long> {
    @Query("SELECT sd FROM SensorData sd WHERE sd.user.email = :email")
    List<SensorData> findByClientEmail(@Param("email") String email);
}
