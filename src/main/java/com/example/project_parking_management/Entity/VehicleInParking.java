package com.example.project_parking_management.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "VehicleInParkings")
@AllArgsConstructor
@NoArgsConstructor
public class VehicleInParking {
    @Id
    private String license_vehicle;
    private String id_card;
    private String parking_name;
    private String type;
    private Timestamp entry_time;
    private String image;

}
