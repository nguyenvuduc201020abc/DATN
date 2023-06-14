package com.example.project_parking_management.Repository;

import com.example.project_parking_management.Entity.MonthTicket;
import com.example.project_parking_management.Entity.Parking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingRepository extends JpaRepository<Parking, String> {

    @Query("SELECT p FROM Parking p WHERE p.parking_name = ?1")
    Parking findByParkingName(String parking_name);
}
