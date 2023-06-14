package com.example.project_parking_management.Repository;

import com.example.project_parking_management.Entity.Account;
import com.example.project_parking_management.Entity.VehicleInParking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleInParkingRepository extends JpaRepository<VehicleInParking, String> {
    VehicleInParking findById_card(String id_card);
}
