package com.example.project_parking_management.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "Managements")
@AllArgsConstructor
@NoArgsConstructor
public class Management {
    @Id
    private String username;
    private int parking_name;

}
