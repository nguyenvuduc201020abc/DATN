package com.example.project_parking_management.Json;

import lombok.Data;

@Data
public class RequestSaveVehicle {
    String id_card;
    String license_vehicle;
    String type;
    String parking_name;
    String image;
}
