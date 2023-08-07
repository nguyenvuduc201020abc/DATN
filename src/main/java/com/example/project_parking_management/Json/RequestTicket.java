package com.example.project_parking_management.Json;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
@Data
@AllArgsConstructor
public class RequestTicket {
    String id_card;
    String license_vehicle;
    String type;
    String username;
    String parking_name;
    String duration;
    Long cost;
}
