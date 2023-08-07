package com.example.project_parking_management.Controller;

import com.example.project_parking_management.Auth.JwtUtil;
import com.example.project_parking_management.Dto.ParkingCostDto;
import com.example.project_parking_management.Encode.PasswordUtil;
import com.example.project_parking_management.Entity.*;
import com.example.project_parking_management.Json.JWT;
import com.example.project_parking_management.Json.RequestTicket;
import com.example.project_parking_management.Service.AccountService;
//import com.example.project_parking_management.Service.MonthTicketService;
//import com.example.project_parking_management.Service.VehicleInParkingService;
import com.example.project_parking_management.Service.MonthTicketService;
import com.example.project_parking_management.Service.OwnerService;
import com.example.project_parking_management.Service.ParkingService;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@ResponseBody
@CrossOrigin
public class UserController {
    @Autowired
    ParkingService parkingService;
    @Autowired
    MonthTicketService monthTicketService;
    @Autowired
    AccountService accountService;
    @Autowired
    OwnerService ownerService;
    private PasswordUtil passwordUtil;

    @Autowired
    public UserController(AccountService accountService) {
        this.accountService = accountService;
    }

    Gson gson = new Gson();

    @PostMapping("/signup-user")
    public ResponseEntity<?> signup(@RequestParam String username, @RequestParam String password) throws SQLException, ClassNotFoundException {
        final String role = "user";
        if (accountService.findByUserName(username) == null) {
            Account accounts = new Account(username, passwordUtil.hashPassword(password), role);
            accountService.saveAccount(accounts);
            return ResponseEntity.ok(accounts);
        } else return ResponseEntity.status(HttpStatus.CONFLICT).body(username);

    }

    @PostMapping("/login-user")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) throws SQLException, ClassNotFoundException {
        Account account = new Account(username, password, "user");
        if (accountService.checkLoginUser(username, password)) {
            String token = JwtUtil.generateToken(username, password, "user");
            JWT jwt = new JWT(token);
            return ResponseEntity.ok(gson.toJson(jwt));
        }
//        else return ResponseEntity.notFound().build();
        else return new ResponseEntity<>(account, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/get_month_ticket")
    public ResponseEntity<?> get_vehicle(@RequestParam String username, @RequestHeader("Authorization") String jwt) throws SQLException, ClassNotFoundException {
        Claims claims = JwtUtil.decodeToken(jwt.replaceFirst("Bearer ", ""));
        String decodedRole = claims.get("role", String.class);
        if (decodedRole.equals("user")) {
            List<MonthTicket> monthTickets = monthTicketService.getListMonthTicket(username);
            return ResponseEntity.ok(gson.toJson(monthTickets));
        } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need user role!");
    }

    @GetMapping("/get-all-vehicles")
    public ResponseEntity<?> getAllVehicles(@RequestParam String username) throws SQLException, ClassNotFoundException {
        List<Owner> owners = ownerService.getListOwner(username);
        List<String> licenseVehicles = new ArrayList<>();
        for (Owner owner : owners) {
            licenseVehicles.add(owner.getLicense_vehicle());
        }
        return ResponseEntity.ok(licenseVehicles);
    }

    @PostMapping("/post_month_ticket")
    public ResponseEntity<?> register_month_ticket(@RequestBody RequestTicket requestTicket, @RequestHeader("Authorization") String jwt) throws SQLException, ClassNotFoundException {
        Claims claims = JwtUtil.decodeToken(jwt.replaceFirst("Bearer ", ""));
        String decodedRole = claims.get("role", String.class);
        if (decodedRole.equals("user")) {
            MonthTicket monthTicket = new MonthTicket();
            monthTicket.setId_card(requestTicket.getId_card());
            monthTicket.setLicense_vehicle(requestTicket.getLicense_vehicle());
            monthTicket.setType(requestTicket.getType());
            monthTicket.setUsername(requestTicket.getUsername());
            monthTicket.setParking_name(requestTicket.getParking_name());
            monthTicket.setDuration(requestTicket.getDuration());
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            monthTicket.setTime_register(currentTimestamp);
            Parking parking = parkingService.getParkingByParking_name(requestTicket.getParking_name());
            if (requestTicket.getType().equals("car")) {
                monthTicket.setCost(parking.getCar_month());
            } else {
                monthTicket.setCost(parking.getMotor_month());
            }
            monthTicketService.saveMonthTicket(monthTicket);
            ParkingCostDto parkingCostDto = new ParkingCostDto();
            parkingCostDto.setCost(monthTicket.getCost());
            return ResponseEntity.ok(gson.toJson(parkingCostDto));
        } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need user role!");
    }

    @GetMapping("/check_month_ticket_user")
    public ResponseEntity<?> check_month_ticket(@RequestBody RequestTicket requestTicket, @RequestHeader("Authorization") String jwt) throws SQLException, ClassNotFoundException {
        Claims claims = JwtUtil.decodeToken(jwt.replaceFirst("Bearer ", ""));
        String decodedRole = claims.get("role", String.class);
        if (decodedRole.equals("user")) {
            ParkingCostDto parkingCostDto = new ParkingCostDto();
            Parking parking = parkingService.getParkingByParking_name(requestTicket.getParking_name());
            if (requestTicket.getType().equals("car")) {
                parkingCostDto.setCost(parking.getCar_month());
                return ResponseEntity.ok(parkingCostDto);
            } else {
                parkingCostDto.setCost(parking.getMotor_month());
                return ResponseEntity.ok(gson.toJson(parkingCostDto));
            }
        } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need user role!");
    }
}


