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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public String signup(@RequestParam String username, @RequestParam String password) throws SQLException, ClassNotFoundException {
        final String role ="user";
        Account accounts = new Account(username,passwordUtil.hashPassword(password),role);
        accountService.saveAccount(accounts);
        return  accounts.toString();
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
}
