package com.example.project_parking_management.Dto;
import lombok.Data;
@Data
public class ParkingDto {
    private String parking_name;
    private String parking_address;
    private int available_motor;
    private int available_car;
    private double distance;
}
