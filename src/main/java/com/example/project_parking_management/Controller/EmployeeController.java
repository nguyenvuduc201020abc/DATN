package com.example.project_parking_management.Controller;

import com.example.project_parking_management.Auth.JwtUtil;
import com.example.project_parking_management.Entity.*;
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
import java.time.temporal.ChronoUnit;

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

//    @PostMapping("/export-bill")
//    public ResponseEntity<?> exportBill(@RequestParam String id_card) throws SQLException, ClassNotFoundException {
//        Bill bill = new Bill();
//        LocalDateTime currentTime = LocalDateTime.now();
//        Timestamp currenTime1 = Timestamp.valueOf(currentTime);
//        VehicleInParking vehicleInParking = vehicleInParkingService.getVehicleById_card(id_card);
//        Timestamp entry_time = vehicleInParking.getEntry_time();
//        String type_vehicle = vehicleInParking.getType();
//        String parking_name = vehicleInParking.getParking_name();
//        Parking parking = parkingService.getParkingByName(parking_name);
//        Long cost;
//        if(type_vehicle.equals("motor")){
//            Date date1 = new Date(currenTime1.getTime());
//            Date date2 = new Date(entry_time.getTime());
//            cost = parking.getMm_price() + parking.getMn_price() * (ChronoUnit.DAYS.between(date1.toInstant(), date2.toInstant()));
//        }
//        else{
//            Date date1 = new Date(currenTime1.getTime());
//            Date date2 = new Date(entry_time.getTime());
//            cost = parking.getCm_price() + parking.getCn_price() * (ChronoUnit.DAYS.between(date1.toInstant(), date2.toInstant()));
//        }
//        bill.setLicense_vehicle(vehicleInParking.getLicense_vehicle());
//        bill.setEntry_time(vehicleInParking.getEntry_time());
//        bill.setOut_time(currenTime1);
//        bill.setParking_name(vehicleInParking.getParking_name());
//        bill.setCost(cost);
//        billService.saveBill(bill);
//        return ResponseEntity.ok(bill);
//    }

    @PostMapping("/add-month_ticket")
        public ResponseEntity<?> addMonthTicket(@RequestParam String id_card, @RequestParam String license_vehicle, @RequestParam String type, @RequestParam String username,@RequestParam String parking_name, @RequestParam Date time_ticket){
        MonthTicket monthTicket = new MonthTicket(id_card, license_vehicle, type, username, parking_name, time_ticket);
        Owner owner = new Owner(license_vehicle,username);
        monthTicketService.saveMonthTicket(monthTicket);
        ownerService.saveOwner(owner);
        return ResponseEntity.ok(monthTicket);
}

}
