package com.example.project_parking_management.Json;

import lombok.Data;

@Data
public class JWT {

    String jwt;

    public JWT(String jwt) {
        this.jwt = jwt;
    }
}
