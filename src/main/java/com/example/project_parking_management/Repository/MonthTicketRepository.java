package com.example.project_parking_management.Repository;

import com.example.project_parking_management.Entity.Account;
import com.example.project_parking_management.Entity.Bill;
import com.example.project_parking_management.Entity.MonthTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;

@Repository
public interface MonthTicketRepository extends JpaRepository<MonthTicket, String> {
    List<MonthTicket> findByUsername(String username);
    @Query("SELECT p FROM MonthTicket p WHERE p.parking_name = ?1")
    List<MonthTicket> findByParking_name(String parking_name);
    @Query("SELECT p FROM MonthTicket p WHERE p.id_card = ?1 AND p.parking_name= ?2 AND p.duration= ?3")
    MonthTicket findTicket(String id_card, String parking_name, String duration);

    @Query("SELECT p FROM MonthTicket p WHERE p.id_card = ?1 ORDER BY p.time_register DESC LIMIT 1")
    MonthTicket findTicketById_card(String id_card);

    @Query("SELECT p FROM MonthTicket p WHERE p.id_card = ?1")
    List<MonthTicket> findAllTicketById_card(String id_card);
}
