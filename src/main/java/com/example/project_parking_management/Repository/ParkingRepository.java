package com.example.project_parking_management.Repository;

import com.example.project_parking_management.Entity.Parking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingRepository extends JpaRepository<Parking, Integer> {

}
