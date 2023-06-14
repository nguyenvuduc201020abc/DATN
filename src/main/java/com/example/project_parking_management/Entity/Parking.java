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
    private Float longitude;
    private Float latitude;

    private int   capacity_motor;
    private int   capacity_car;

//    public Parking(String parking_name, String parking_address, Long mm_price, Long mn_price, Long cm_price, Long cn_price, Float longitude, Float latitude, int capacity_motor, int capacity_car) {
//        this.parking_name = parking_name;
//        this.parking_address = parking_address;
//        this.mm_price = mm_price;
//        this.mn_price = mn_price;
//        this.cm_price = cm_price;
//        this.cn_price = cn_price;
//        this.longitude = longitude;
//        this.latitude = latitude;
//        this.capacity_motor = capacity_motor;
//        this.capacity_car = capacity_car;
//    }
}
