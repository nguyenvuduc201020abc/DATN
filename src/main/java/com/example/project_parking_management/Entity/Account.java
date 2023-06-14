package com.example.project_parking_management.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "Accounts")
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    private String username;
    private String password;
    private String role;
}
