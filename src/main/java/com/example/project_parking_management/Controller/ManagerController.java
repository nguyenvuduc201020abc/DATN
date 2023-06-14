package com.example.project_parking_management.Controller;

import com.example.project_parking_management.Auth.JwtUtil;
import com.example.project_parking_management.Encode.PasswordUtil;
import com.example.project_parking_management.Entity.Account;
import com.example.project_parking_management.Entity.Management;
import com.example.project_parking_management.Entity.MonthTicket;
import com.example.project_parking_management.Entity.Parking;
import com.example.project_parking_management.Service.AccountService;
//import com.example.project_parking_management.Service.MonthTicketService;
//import com.example.project_parking_management.Service.ParkingService;
import com.example.project_parking_management.Service.ManagementService;
import com.example.project_parking_management.Service.MonthTicketService;
import com.example.project_parking_management.Service.ParkingService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.YearMonth;

@RestController
@ResponseBody
@CrossOrigin
public class ManagerController {
    @Autowired
    ManagementService managementService;
    @Autowired
    MonthTicketService monthTicketService;
    private PasswordUtil passwordUtil;
    @Autowired
    AccountService accountService;
    @Autowired
    ParkingService parkingService;
    @PostMapping("/login-manager")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) throws SQLException, ClassNotFoundException {
        if (accountService.checkLoginManage(username, password)){
            String token = JwtUtil.generateToken(username, password, "manager");
            return ResponseEntity.ok(token);
        }
        else return ResponseEntity.notFound().build();
    }

    @PostMapping("/add-parking")
    public ResponseEntity<?> addParking(@RequestParam String parking_name, @RequestParam String parking_address, @RequestParam Long mm_price, @RequestParam Long mn_price,@RequestParam  Long cm_price,@RequestParam  Long cn_price,@RequestParam Float longitude,@RequestParam Float latitude, @RequestParam int capacity_motor, @RequestParam int capacity_car, @RequestHeader("Authorization") String jwt){
        Claims claims = JwtUtil.decodeToken(jwt);
        String decodedRole = claims.get("role", String.class);
        if(decodedRole.equals("manager")){
            Parking parking = new Parking(parking_name, parking_address, mm_price, mn_price, cm_price, cn_price, longitude, latitude, capacity_motor, capacity_car);
            parkingService.saveParking(parking);
            return ResponseEntity.ok(parking);
        }
        else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need admin role!");
    }

    @PostMapping("/add-employee")
    public ResponseEntity<?> addEmployee(@RequestParam String parking_name, @RequestParam String username, @RequestParam String password, @RequestHeader("Authorization") String jwt){
        Claims claims = JwtUtil.decodeToken(jwt);
        String decodedRole = claims.get("role", String.class);
        if(decodedRole.equals("manager")){
            Management management = new Management(username , parking_name);
            managementService.saveManagement(management);
            Account account = new Account(username, password, "employee");
            accountService.saveAccount(account);
            return ResponseEntity.ok(management);
        }
        else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need admin role!");
    }


    @PostMapping("/signup-manager")
    public String signup(@RequestParam String username, @RequestParam String password) throws SQLException, ClassNotFoundException {
        final String role ="manager";
        Account accounts = new Account(username,passwordUtil.hashPassword(password),role);
        accountService.saveAccount(accounts);
        return  accounts.toString();
    }




}
