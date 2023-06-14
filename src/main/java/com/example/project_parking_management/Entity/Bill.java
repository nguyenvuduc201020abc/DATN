package com.example.project_parking_management.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "Bills")
@AllArgsConstructor
@NoArgsConstructor
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bill_id;
    private String license_vehicle;
    private Date entry_time;
    private String username;
    private Timestamp out_time;
    private int parking_code;
    private int cost;

}
