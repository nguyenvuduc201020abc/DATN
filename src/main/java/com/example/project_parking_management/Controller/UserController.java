package com.example.project_parking_management.Controller;

import com.example.project_parking_management.Auth.JwtUtil;
import com.example.project_parking_management.Encode.PasswordUtil;
import com.example.project_parking_management.Entity.Account;
import com.example.project_parking_management.Entity.MonthTicket;
import com.example.project_parking_management.Entity.VehicleInParking;
import com.example.project_parking_management.Json.JWT;
import com.example.project_parking_management.Service.AccountService;
//import com.example.project_parking_management.Service.MonthTicketService;
//import com.example.project_parking_management.Service.VehicleInParkingService;
import com.example.project_parking_management.Service.MonthTicketService;
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
import java.util.List;

@RestController
@ResponseBody
@CrossOrigin
public class UserController {
    @Autowired
    MonthTicketService monthTicketService;
////    private final AccountService accountService;
//    @Autowired
//    VehicleInParkingService vehicleInParkingService;
    @Autowired
    AccountService accountService;
    private PasswordUtil passwordUtil;
    @Autowired
    public UserController(AccountService accountService) {
        this.accountService = accountService;
    }
    Gson gson = new Gson();

    @PostMapping("/signup-user")
        public ResponseEntity<?> signup(@RequestParam String username, @RequestParam String password) throws SQLException, ClassNotFoundException {
        final String role ="user";
        if(accountService.findByUserName(username)==null){
        Account accounts = new Account(username,passwordUtil.hashPassword(password),role);
            accountService.saveAccount(accounts);
            return  ResponseEntity.ok(accounts);
        }
        else return ResponseEntity.status(HttpStatus.CONFLICT).body(username);

    }
    @PostMapping("/login-user")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) throws SQLException, ClassNotFoundException {
        Account account = new Account(username, password, "user");
        if (accountService.checkLoginUser(username, password)){
            String token = JwtUtil.generateToken(username, password, "user");
            JWT jwt = new JWT(token);
            return ResponseEntity.ok(gson.toJson(jwt));
        }
//        else return ResponseEntity.notFound().build();
        else return new ResponseEntity<>(account, HttpStatus.NOT_FOUND);
    }
    @GetMapping("/get_month_ticket")
    public ResponseEntity<?> get_vehicle(@RequestParam String username, @RequestHeader("Authorization") String jwt) throws SQLException, ClassNotFoundException {
        Claims claims = JwtUtil.decodeToken(jwt);
        String decodedRole = claims.get("role", String.class);
        if(decodedRole.equals("user")){
            List<MonthTicket> monthTickets = monthTicketService.getListMonthTicket(username);
            return ResponseEntity.ok(gson.toJson(monthTickets) );
        }
        else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need user role!");
//        List<MonthTicket> monthTickets = monthTicketService.getListMonthTicket(username);
//        return ResponseEntity.ok(monthTickets);
//        List<VehicleInParking> vehicleInParkings = vehicleInParkingService.getVehicleByUsername(username);
//        return ResponseEntity.ok(vehicleInParkings);
    }
    @PostMapping ("/post_month_ticket")
    public ResponseEntity<?> register_month_ticket(@RequestBody MonthTicket monthTicket, @RequestHeader("Authorization") String jwt ) throws SQLException, ClassNotFoundException {
        Claims claims = JwtUtil.decodeToken(jwt);
        String decodedRole = claims.get("role", String.class);
        if(decodedRole.equals("user")){
            monthTicketService.saveMonthTicket(monthTicket);
            return ResponseEntity.ok(gson.toJson(monthTicket));
        }
        else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need user role!");
//        MonthTicket monthTicket = new MonthTicket(id_card, license_vehicle,type, username , parking_name, time_ticket);
//        monthTicketService.saveMonthTicket(monthTicket);
//        return ResponseEntity.ok("aaaa");
    }
}


