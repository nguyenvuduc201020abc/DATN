package com.example.project_parking_management.Dto;

import lombok.Data;

import java.sql.Date;

@Data
public class VehicleInParkingDto {
    private String license_vehicle;
    private String id_card;
    private int parking_code;
    private String type;
    private Date entry_time;
}
