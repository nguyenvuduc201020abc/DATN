package com.example.project_parking_management.Controller;

import com.example.project_parking_management.Auth.JwtUtil;
import com.example.project_parking_management.Encode.PasswordUtil;
import com.example.project_parking_management.Entity.Account;
import com.example.project_parking_management.Entity.MonthTicket;
import com.example.project_parking_management.Entity.VehicleInParking;
import com.example.project_parking_management.Service.AccountService;
//import com.example.project_parking_management.Service.MonthTicketService;
//import com.example.project_parking_management.Service.VehicleInParkingService;
import com.example.project_parking_management.Service.MonthTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.SQLException;
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

    @PostMapping("/signup-user")
    public ResponseEntity<?> signup(@RequestParam String username, @RequestParam String password) throws SQLException, ClassNotFoundException {
        final String role ="user";
        if(accountService.findByUserName(username)!=null){
        Account accounts = new Account(username,passwordUtil.hashPassword(password),role);
            accountService.saveAccount(accounts);
            return  ResponseEntity.ok(accounts);
        }
        else return ResponseEntity.status(HttpStatus.CONFLICT).body(username);

    }
    @PostMapping("/login-user")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) throws SQLException, ClassNotFoundException {
        if (accountService.checkLoginUser(username, password)){
            String token = JwtUtil.generateToken(username, password, "user");
            return ResponseEntity.ok(token);
        }
        else return ResponseEntity.notFound().build();
    }
    @GetMapping("/get-month_ticket")
    public ResponseEntity<?> get_vehicle(@RequestParam String username) throws SQLException, ClassNotFoundException {
        List<MonthTicket> monthTickets = monthTicketService.getListMonthTicket(username);
        return ResponseEntity.ok(monthTickets);
//        List<VehicleInParking> vehicleInParkings = vehicleInParkingService.getVehicleByUsername(username);
//        return ResponseEntity.ok(vehicleInParkings);
    }
    @PostMapping ("/post_month_ticket")
    public ResponseEntity<?> register_month_ticket(@RequestParam String id_card, @RequestParam String license_vehicle,@RequestParam String type,@RequestParam String username,@RequestParam String parking_name,@RequestParam Date time_ticket ) throws SQLException, ClassNotFoundException {
        MonthTicket monthTicket = new MonthTicket(id_card, license_vehicle,type, username , parking_name, time_ticket);
        monthTicketService.saveMonthTicket(monthTicket);
        return ResponseEntity.ok("a");
    }
}

//    String id_card;
//    String license_vehicle;
//    String type;
//    String username;
//    String parking_name;
//    //    Timestamp time_ticket;
//    Date time_ticket;
