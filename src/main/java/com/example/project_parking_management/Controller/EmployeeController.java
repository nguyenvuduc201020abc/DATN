package com.example.project_parking_management.Controller;

import com.example.project_parking_management.Auth.JwtUtil;
import com.example.project_parking_management.Entity.EntryVehicle;
import com.example.project_parking_management.Entity.MonthTicket;
import com.example.project_parking_management.Entity.Owner;
import com.example.project_parking_management.Entity.VehicleInParking;
import com.example.project_parking_management.Service.*;
//import com.example.project_parking_management.Service.EntryVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Controller
@ResponseBody
@CrossOrigin
public class EmployeeController {
    @Autowired
    BillService billService;
    @Autowired
    MonthTicketService monthTicketService;
    @Autowired
    VehicleInParkingService vehicleInParkingService;
    @Autowired
    EntryVehicleService entryVehicleService;
    @Autowired
    OwnerService ownerService;
    @Autowired
    AccountService accountService;
    @PostMapping("/login-employee")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) throws SQLException, ClassNotFoundException {
        if (accountService.checkLoginEmployee(username, password)){
            String token = JwtUtil.generateToken(username, password, "manager");
            return ResponseEntity.ok(token);
        }
        else return ResponseEntity.notFound().build();
    }
    @PostMapping("/save-vehicle")
        public ResponseEntity<?> saveVehicle(@RequestParam String license_vehicle , @RequestParam String id_card, @RequestParam String parking_name, @RequestParam String type, @RequestParam String image) throws SQLException, ClassNotFoundException {
        LocalDateTime currentTime = LocalDateTime.now();
        Timestamp currenTime1 = Timestamp.valueOf(currentTime);
        VehicleInParking vehicleInParking = new VehicleInParking(license_vehicle, id_card, parking_name, type, currenTime1, image);
        vehicleInParkingService.saveVehicleInParking(vehicleInParking);
        EntryVehicle entryVehicle = new EntryVehicle(license_vehicle, currenTime1, image, parking_name, type);
        entryVehicleService.saveEntryVehicle(entryVehicle);
        return ResponseEntity.ok(vehicleInParking);
    }

    @PostMapping("/export-bill")
    public ResponseEntity<?> exportBill(@RequestParam String id_card) throws SQLException, ClassNotFoundException {
        LocalDateTime currentTime = LocalDateTime.now();
        Timestamp currenTime1 = Timestamp.valueOf(currentTime);
        VehicleInParking vehicleInParking = vehicleInParkingService.getVehicleById_card(id_card);
        Timestamp entry_time = vehicleInParking.getEntry_time();
//        VehicleInParking vehicleInParking = new VehicleInParking(license_vehicle, id_card, parking_name, type, currenTime1, image);
//        vehicleInParkingService.saveVehicleInParking(vehicleInParking);
//        EntryVehicle entryVehicle = new EntryVehicle(license_vehicle, currenTime1, image, parking_name, type);
//        entryVehicleService.saveEntryVehicle(entryVehicle);
        return ResponseEntity.ok(vehicleInParking);
    }

    @PostMapping("/add-month_ticket")
        public ResponseEntity<?> addMonthTicket(@RequestParam String id_card, @RequestParam String license_vehicle, @RequestParam String type, @RequestParam String username,@RequestParam String parking_name, @RequestParam Date time_ticket){
        MonthTicket monthTicket = new MonthTicket(id_card, license_vehicle, type, username, parking_name, time_ticket);
        Owner owner = new Owner(license_vehicle,username);
        monthTicketService.saveMonthTicket(monthTicket);
        ownerService.saveOwner(owner);
        return ResponseEntity.ok(monthTicket);
}

}
