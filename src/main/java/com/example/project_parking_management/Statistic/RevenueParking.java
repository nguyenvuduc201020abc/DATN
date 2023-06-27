package com.example.project_parking_management.Statistic;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class RevenueParking {
    String parking_name;
    Timestamp beginTime;
    Timestamp endTime;
    Long revenueAll;
}
