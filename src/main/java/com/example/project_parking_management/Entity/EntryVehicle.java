package com.example.project_parking_management.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "Entry_Vehicles")
@AllArgsConstructor
@NoArgsConstructor
public class EntryVehicle {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long bill_id;
    private String license_vehicle;
    private Timestamp entry_time;
    private String image;
    private String parking_name;
    private String type;

}
