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
    private Timestamp entry_time;
//    private String username;
    private Timestamp out_time;
    private String parking_name;
    private Long cost;

//    public Bill(String license_vehicle, Date entry_time, String username, Timestamp out_time, int parking_code, Long cost) {
//        this.license_vehicle = license_vehicle;
//        this.entry_time = entry_time;
//        this.username = username;
//        this.out_time = out_time;
//        this.parking_code = parking_code;
//        this.cost = cost;
//    }
}
