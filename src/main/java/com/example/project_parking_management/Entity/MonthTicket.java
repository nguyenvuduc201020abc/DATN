package com.example.project_parking_management.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    String id_card;
    String license_vehicle;
    String type;
    String username;
    String parking_name;
//    Timestamp time_ticket;
    Date time_ticket;

}
