package com.example.project_parking_management.Dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class BillDto {
    private String license_vehicle;
    private String type;
    private Timestamp entry_time;
    //    private String username;
    private String parking_name;
    private String image;
    private Long cost;
}
