package com.example.project_parking_management.Dto;

import lombok.Data;

@Data
public class ParkingDto {
    private String parking_name;
    private String parking_address;
    private int capacity_motor;
    private int capacity_car;
    private Double longtitude;
    private Double latitude;
    private double distance;
}
