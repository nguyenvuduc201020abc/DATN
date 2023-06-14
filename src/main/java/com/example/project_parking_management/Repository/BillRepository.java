package com.example.project_parking_management.Repository;

import com.example.project_parking_management.Entity.Bill;
import com.example.project_parking_management.Entity.Parking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
//    List<Bill> findByParking_name(String parking_name);
    @Query("SELECT p FROM Bill p WHERE p.parking_name = ?1")
    List<Bill> findByParkingName(String parking_name);
}
