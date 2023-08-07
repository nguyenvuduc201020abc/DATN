package com.example.project_parking_management.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "Parkings")
@AllArgsConstructor
@NoArgsConstructor
public class Parking {
    @Id
    private String parking_name;
    private String parking_address;
    private Long mm_price;
    private Long mn_price;
    private Long cm_price;
    private Long cn_price;



    private Long car_month;
    private Long motor_month;
    private Double longitude;
    private Double latitude;
    private int   capacity_motor;
    private int   capacity_car;

}
