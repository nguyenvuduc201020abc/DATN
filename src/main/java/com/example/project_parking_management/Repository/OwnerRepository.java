package com.example.project_parking_management.Repository;

import com.example.project_parking_management.Entity.MonthTicket;
import com.example.project_parking_management.Entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, String> {
    List<Owner> findByUsername(String username);
}
