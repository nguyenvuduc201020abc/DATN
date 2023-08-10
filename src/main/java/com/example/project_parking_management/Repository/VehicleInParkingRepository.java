package com.example.project_parking_management.Repository;

import com.example.project_parking_management.Entity.Account;
import com.example.project_parking_management.Entity.Parking;
import com.example.project_parking_management.Entity.VehicleInParking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleInParkingRepository extends JpaRepository<VehicleInParking, String> {
//    VehicleInParking findById_card(String id_card);
    @Query("SELECT p FROM VehicleInParking p WHERE p.id_card = ?1")
    VehicleInParking findByIdCard(String id_card);
    @Query("SELECT p FROM VehicleInParking p WHERE p.parking_name = ?1")
    List<VehicleInParking> findByParking_name(String parking_name);
}
