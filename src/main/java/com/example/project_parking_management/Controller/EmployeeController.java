package com.example.project_parking_management.Controller;

import com.example.project_parking_management.Auth.JwtUtil;
import com.example.project_parking_management.Entity.*;
import com.example.project_parking_management.Service.*;
//import com.example.project_parking_management.Service.EntryVehicleService;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Controller
@ResponseBody
@CrossOrigin
public class EmployeeController {
    @Autowired
    ParkingService parkingService;
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
    Gson gson = new Gson();
    @PostMapping("/login-employee")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) throws SQLException, ClassNotFoundException {
        if (accountService.checkLoginEmployee(username, password)){
            String token = JwtUtil.generateToken(username, password, "manager");
            return ResponseEntity.ok(token);
        }
        else return ResponseEntity.notFound().build();
    }
    @PostMapping("/save-vehicle")
        public ResponseEntity<?> saveVehicle(@RequestParam String license_vehicle , @RequestParam String id_card, @RequestParam String parking_name, @RequestParam String type, @RequestParam String image, @RequestHeader("Authorization") String jwt) throws SQLException, ClassNotFoundException {
            Claims claims = JwtUtil.decodeToken(jwt);
            String decodedRole = claims.get("role", String.class);
            if(decodedRole.equals("employee")){
                LocalDateTime currentTime = LocalDateTime.now();
                Timestamp currenTime1 = Timestamp.valueOf(currentTime);
                VehicleInParking vehicleInParking = new VehicleInParking(license_vehicle, id_card, parking_name, type, currenTime1, image);
                vehicleInParkingService.saveVehicleInParking(vehicleInParking);
                EntryVehicle entryVehicle = new EntryVehicle(license_vehicle, currenTime1, image, parking_name, type);
                entryVehicleService.saveEntryVehicle(entryVehicle);
                return ResponseEntity.ok(vehicleInParking);
                }
            else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need employee role!");
        }

    @PostMapping("/export-bill")
    public ResponseEntity<?> exportBill(@RequestParam String id_card, @RequestHeader("Authorization") String jwt) throws SQLException, ClassNotFoundException {
        Claims claims = JwtUtil.decodeToken(jwt);
        String decodedRole = claims.get("role", String.class);
        if(decodedRole.equals("employee")){
            Bill bill = new Bill();
            java.util.Date currenDate = new java.util.Date();
            Timestamp currenTime1 = new Timestamp(currenDate.getTime());
            VehicleInParking vehicleInParking = vehicleInParkingService.getVehicleById_card(id_card);
            Timestamp entry_time = vehicleInParking.getEntry_time();
            String type_vehicle = vehicleInParking.getType();
            String parking_name = vehicleInParking.getParking_name();
            Parking parking = parkingService.getParkingByParking_name(parking_name);
            Long cost;
            if(type_vehicle.equals("motor")){
                java.util.Date date1 = new java.util.Date(currenTime1.getTime());
                java.util.Date date2 = new java.util.Date(entry_time.getTime());
                cost = parking.getMm_price() + parking.getMn_price() * (ChronoUnit.DAYS.between(date1.toInstant(), date2.toInstant()));
            }
            else{
                java.util.Date date1 = new java.util.Date(currenTime1.getTime());
                java.util.Date date2 = new java.util.Date(entry_time.getTime());
                cost = parking.getCm_price() + parking.getCn_price() * (ChronoUnit.DAYS.between(date1.toInstant(), date2.toInstant()));
            }
            bill.setLicense_vehicle(vehicleInParking.getLicense_vehicle());
            bill.setEntry_time(vehicleInParking.getEntry_time());
            bill.setOut_time(currenTime1);
            bill.setParking_name(vehicleInParking.getParking_name());
            bill.setCost(cost);
            billService.saveBill(bill);
            vehicleInParkingService.deleteVehicleInParking(vehicleInParking);
            return ResponseEntity.ok(bill);
        }
        else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need employee role!");
//        Bill bill = new Bill();
//        java.util.Date currenDate = new java.util.Date();
//        Timestamp currenTime1 = new Timestamp(currenDate.getTime());
//        VehicleInParking vehicleInParking = vehicleInParkingService.getVehicleById_card(id_card);
//        Timestamp entry_time = vehicleInParking.getEntry_time();
//        String type_vehicle = vehicleInParking.getType();
//        String parking_name = vehicleInParking.getParking_name();
//        Parking parking = parkingService.getParkingByParking_name(parking_name);
//        Long cost;
//        if(type_vehicle.equals("motor")){
//            java.util.Date date1 = new java.util.Date(currenTime1.getTime());
//            java.util.Date date2 = new java.util.Date(entry_time.getTime());
//            cost = parking.getMm_price() + parking.getMn_price() * (ChronoUnit.DAYS.between(date1.toInstant(), date2.toInstant()));
//        }
//        else{
//            java.util.Date date1 = new java.util.Date(currenTime1.getTime());
//            java.util.Date date2 = new java.util.Date(entry_time.getTime());
//            cost = parking.getCm_price() + parking.getCn_price() * (ChronoUnit.DAYS.between(date1.toInstant(), date2.toInstant()));
//        }
//        bill.setLicense_vehicle(vehicleInParking.getLicense_vehicle());
//        bill.setEntry_time(vehicleInParking.getEntry_time());
//        bill.setOut_time(currenTime1);
//        bill.setParking_name(vehicleInParking.getParking_name());
//        bill.setCost(cost);
//        billService.saveBill(bill);
//        vehicleInParkingService.deleteVehicleInParking(vehicleInParking);
//        return ResponseEntity.ok(bill);
    }


@PostMapping("/add_month_ticket")
public ResponseEntity<?> addMonthTicket(@RequestBody MonthTicket monthTicket, @RequestHeader("Authorization") String jwt){
    Claims claims = JwtUtil.decodeToken(jwt);
    String decodedRole = claims.get("role", String.class);
    if(decodedRole.equals("employee")){
        LocalDateTime time = LocalDateTime.now();
        MonthTicket monthTicket1 = monthTicket;
//    Owner owner = new Owner(license_vehicle,username);
        monthTicketService.saveMonthTicket(monthTicket);
//    ownerService.saveOwner(owner);
        return ResponseEntity.ok(gson.toJson(monthTicket1));
    }
    else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need employee role!");
//    LocalDateTime time = LocalDateTime.now();
//    MonthTicket monthTicket1 = monthTicket;
////    Owner owner = new Owner(license_vehicle,username);
//    monthTicketService.saveMonthTicket(monthTicket);
////    ownerService.saveOwner(owner);
//    return ResponseEntity.ok(gson.toJson(monthTicket1));
}

}
