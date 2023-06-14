package com.example.project_parking_management.Repository;

import com.example.project_parking_management.Entity.Account;
import com.example.project_parking_management.Entity.MonthTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;

@Repository
public interface MonthTicketRepository extends JpaRepository<MonthTicket, String> {
    List<MonthTicket> findByUsername(String username);
}
