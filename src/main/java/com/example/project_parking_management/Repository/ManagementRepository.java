package com.example.project_parking_management.Repository;

import com.example.project_parking_management.Entity.Management;
import com.example.project_parking_management.Entity.VehicleInParking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagementRepository extends JpaRepository<Management, String> {
    @Query("SELECT p FROM Management p WHERE p.username = ?1")
    Management findByUsername(String username);

//    @Query("UPDATE Management p SET p.parking_name = : parking_name WHERE p.username = :username")
//    void updatePriceById(String username, String parking_name);

}
