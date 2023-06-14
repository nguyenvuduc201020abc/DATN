package com.example.project_parking_management.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "owner")
@AllArgsConstructor
@NoArgsConstructor
public class Owner {
    @Id
    private String license_vehicle;
    private String username;
}
