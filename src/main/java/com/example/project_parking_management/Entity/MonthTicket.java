package com.example.project_parking_management.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Entity
@Data
@Table(name = "MonthTicket")
@AllArgsConstructor
@NoArgsConstructor
public class MonthTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long month_ticket_id;
    String id_card;
    String license_vehicle;
    String type;
    String username;
    String parking_name;
//    Timestamp time_ticket;
    Timestamp time;
}
