package com.example.project_parking_management.Dto;

import lombok.Data;

@Data
public class ParkingEmDto {
    private String parking_name;
    private String parking_address;
    private int capacity_motor;
    private int capacity_car;
    private int empty_car;
    private int empty_motor;
    private Long car_month;
    private Long motor_month;
    private Double longtitude;
    private Double latitude;
    private double distance;
}
