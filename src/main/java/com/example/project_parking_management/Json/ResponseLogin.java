package com.example.project_parking_management.Json;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseLogin {
    String accessToken;
    Integer role;
    String parkingName;
}
