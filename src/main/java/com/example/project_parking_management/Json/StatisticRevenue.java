package com.example.project_parking_management.Json;

import lombok.Data;

@Data
public class StatisticRevenue implements Comparable<StatisticRevenue> {
    String parking_name;
    Long revenue;

    @Override
    public int compareTo(StatisticRevenue statisticRevenue) {
        return (int)(this.getRevenue() - statisticRevenue.getRevenue());
    }
}
