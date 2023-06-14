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
@Table(name = "Entry_Vehicles")
@AllArgsConstructor
@NoArgsConstructor
public class EntryVehicle {
    @Id
    private String license_vehicle;
    private Timestamp entry_time;
    private String image;
    private String parking_name;
    private String type;

}
