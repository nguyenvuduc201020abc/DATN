package com.example.project_parking_management.Controller;

import com.example.project_parking_management.Auth.JwtUtil;
import com.example.project_parking_management.Dto.BillDto;
import com.example.project_parking_management.Dto.ParkingCostDto;
import com.example.project_parking_management.Entity.*;
import com.example.project_parking_management.Json.RequestSaveVehicle;
import com.example.project_parking_management.Json.RequestTicket;
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
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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

    //    @PostMapping("/login-employee")
//    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) throws SQLException, ClassNotFoundException {
//        if (accountService.checkLoginEmployee(username, password)){
//            String token = JwtUtil.generateToken(username, password, "manager");
//            return ResponseEntity.ok(token);
//        }
//        else return ResponseEntity.notFound().build();
//    }
    @PostMapping("/save-vehicle")
    public ResponseEntity<?> saveVehicle(@RequestParam String license_vehicle, @RequestParam String id_card, @RequestParam String parking_name, @RequestParam String type, @RequestParam String image, @RequestHeader("Authorization") String jwt) throws SQLException, ClassNotFoundException {
        Claims claims = JwtUtil.decodeToken(jwt.replaceFirst("Bearer ", ""));
        String decodedRole = claims.get("role", String.class);
        if (decodedRole.equals("employee")) {
            LocalDateTime currentTime = LocalDateTime.now();
            Timestamp currenTime1 = Timestamp.valueOf(currentTime);
//            Timestamp currenTime2 = Timestamp.valueOf("2023-06-29 12:34:56");
            VehicleInParking vehicleInParking = new VehicleInParking(license_vehicle, id_card, parking_name, type, currenTime1, image);
            vehicleInParkingService.saveVehicleInParking(vehicleInParking);
            EntryVehicle entryVehicle = new EntryVehicle(license_vehicle, currenTime1, image, parking_name, type);
            entryVehicleService.saveEntryVehicle(entryVehicle);
            return ResponseEntity.ok(vehicleInParking);
        } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need employee role!");
    }

    @PostMapping("/save-vehicle1")
    public ResponseEntity<?> saveVehicle(@RequestBody RequestSaveVehicle requestSaveVehicle) throws SQLException, ClassNotFoundException {

            LocalDateTime currentTime = LocalDateTime.now();
            Timestamp currenTime1 = Timestamp.valueOf(currentTime);
//            Timestamp currenTime2 = Timestamp.valueOf("2023-06-29 12:34:56");
            VehicleInParking vehicleInParking = new VehicleInParking(requestSaveVehicle.getLicense_vehicle(), requestSaveVehicle.getId_card(), requestSaveVehicle.getParking_name(), requestSaveVehicle.getType(), currenTime1, requestSaveVehicle.getImage());
            vehicleInParkingService.saveVehicleInParking(vehicleInParking);
            EntryVehicle entryVehicle = new EntryVehicle(requestSaveVehicle.getLicense_vehicle(), currenTime1, requestSaveVehicle.getImage(), requestSaveVehicle.getParking_name(), requestSaveVehicle.getType());
            entryVehicleService.saveEntryVehicle(entryVehicle);
            return ResponseEntity.ok(vehicleInParking);
    }



    @GetMapping("/export-bill")
    public ResponseEntity<?> exportBill(@RequestParam String id_card, @RequestHeader("Authorization") String jwt) throws SQLException, ClassNotFoundException {

        Claims claims = JwtUtil.decodeToken(jwt.replaceFirst("Bearer ", ""));
        String decodedRole = claims.get("role", String.class);
        if (decodedRole.equals("employee")) {

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(timestamp);

            int month = calendar.get(Calendar.MONTH) + 1;
            int year = calendar.get(Calendar.YEAR);
            String duration = month + "-" + year;


            BillDto bill = new BillDto();
            java.util.Date currenDate = new java.util.Date();
            Timestamp currenTime1 = new Timestamp(currenDate.getTime());
            VehicleInParking vehicleInParking = vehicleInParkingService.getVehicleById_card(id_card);
            Timestamp entry_time = vehicleInParking.getEntry_time();

//            calendar.setTimeInMillis(entry_time.getTime());
//            calendar.setTimeZone(TimeZone.getTimeZone("GMT+7"));
//
//            Timestamp entry_time1 = new Timestamp(calendar.getTimeInMillis());


            String type_vehicle = vehicleInParking.getType();
            String parking_name = vehicleInParking.getParking_name();
            Parking parking = parkingService.getParkingByParking_name(parking_name);
            Long cost=0L;
            if (monthTicketService.checkTicket(id_card, parking_name, duration)) {
                System.out.println("1");
                cost = 0L;
            } else {
                System.out.println("2");
                if (type_vehicle.toLowerCase().equals("motor")) {
                    System.out.println("3");
                    java.util.Date date1 = new java.util.Date(currenTime1.getTime());
                    java.util.Date date2 = new java.util.Date(entry_time.getTime());
//                    System.out.println(ChronoUnit.DAYS.between(date1.toInstant(), date2.toInstant()));
//                    System.out.println(date1.getDate() - date2.getDate());
                    cost = parking.getMm_price() + parking.getMn_price() * (date1.getDate() - date2.getDate());
                } else {
                    System.out.println("4");
                    java.util.Date date1 = new java.util.Date(currenTime1.getTime());
                    java.util.Date date2 = new java.util.Date(entry_time.getTime());
                    System.out.println(ChronoUnit.DAYS.between(date1.toInstant(), date2.toInstant()));
                    cost = parking.getCm_price() + parking.getCn_price() * (date1.getDate() - date2.getDate());
                }
            }
            bill.setLicense_vehicle(vehicleInParking.getLicense_vehicle());
            bill.setEntry_time(entry_time);
            bill.setParking_name(vehicleInParking.getParking_name());
            bill.setImage(vehicleInParking.getImage());
            bill.setCost(cost);
            bill.setType(type_vehicle);
            System.out.println(bill);
//            billService.saveBill(bill);
//            vehicleInParkingService.deleteVehicleInParking(vehicleInParking);
            return ResponseEntity.ok(bill);
        } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need employee role!");
    }


//    @PostMapping("/save-bill")
//    public ResponseEntity<?> saveBill(@RequestParam String id_card, @RequestHeader("Authorization") String jwt) throws SQLException, ClassNotFoundException {
//        Claims claims = JwtUtil.decodeToken(jwt.replaceFirst("Bearer ", ""));
//        String decodedRole = claims.get("role", String.class);
//        if (decodedRole.equals("employee")) {
//
//            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(timestamp);
//
//            int month = calendar.get(Calendar.MONTH) + 1;
//            int year = calendar.get(Calendar.YEAR);
//            String duration = month + "-" + year;
//
//            Bill bill = new Bill();
//            java.util.Date currenDate = new java.util.Date();
//            Timestamp currenTime1 = new Timestamp(currenDate.getTime());
//            VehicleInParking vehicleInParking = vehicleInParkingService.getVehicleById_card(id_card);
//            Timestamp entry_time = vehicleInParking.getEntry_time();
//            String type_vehicle = vehicleInParking.getType();
//            String parking_name = vehicleInParking.getParking_name();
//            Parking parking = parkingService.getParkingByParking_name(parking_name);
//            Long cost;
//            if (monthTicketService.checkTicket(id_card, parking_name, duration)) {
//                cost = 0L;
//            } else {
//                if (type_vehicle.equals("motor")) {
//                    java.util.Date date1 = new java.util.Date(currenTime1.getTime());
//                    java.util.Date date2 = new java.util.Date(entry_time.getTime());
//                    System.out.println(ChronoUnit.DAYS.between(date1.toInstant(), date2.toInstant()));
//                    cost = parking.getMm_price() + parking.getMn_price() * (date1.getDate() - date2.getDate());
//                } else {
//                    java.util.Date date1 = new java.util.Date(currenTime1.getTime());
//                    java.util.Date date2 = new java.util.Date(entry_time.getTime());
//                    System.out.println(ChronoUnit.DAYS.between(date1.toInstant(), date2.toInstant()));
//                    cost = parking.getCm_price() + parking.getCn_price() * (date1.getDate() - date2.getDate());
//                }
//            }
//            bill.setLicense_vehicle(vehicleInParking.getLicense_vehicle());
//            bill.setEntry_time(vehicleInParking.getEntry_time());
//            bill.setOut_time(currenTime1);
//            bill.setParking_name(vehicleInParking.getParking_name());
//            bill.setCost(cost);
//            bill.setType(type_vehicle);
//            billService.saveBill(bill);
//            vehicleInParkingService.deleteVehicleInParking(vehicleInParking);
//            return ResponseEntity.ok(bill);
//        } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need employee role!");
//    }
@PostMapping("/save-bill")
public ResponseEntity<?> saveBill(@RequestParam String id_card) throws SQLException, ClassNotFoundException {


        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timestamp);

        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        String duration = month + "-" + year;

        Bill bill = new Bill();
        java.util.Date currenDate = new java.util.Date();
        Timestamp currenTime1 = new Timestamp(currenDate.getTime());
        VehicleInParking vehicleInParking = vehicleInParkingService.getVehicleById_card(id_card);
        Timestamp entry_time = vehicleInParking.getEntry_time();
        String type_vehicle = vehicleInParking.getType();
        String parking_name = vehicleInParking.getParking_name();
        Parking parking = parkingService.getParkingByParking_name(parking_name);
        Long cost;
        if (monthTicketService.checkTicket(id_card, parking_name, duration)) {
            cost = 0L;
        } else {
            if (type_vehicle.toLowerCase().equals("motor")) {
                java.util.Date date1 = new java.util.Date(currenTime1.getTime());
                java.util.Date date2 = new java.util.Date(entry_time.getTime());
                System.out.println(ChronoUnit.DAYS.between(date1.toInstant(), date2.toInstant()));
                cost = parking.getMm_price() + parking.getMn_price() * (date1.getDate() - date2.getDate());
            } else {
                java.util.Date date1 = new java.util.Date(currenTime1.getTime());
                java.util.Date date2 = new java.util.Date(entry_time.getTime());
                System.out.println(ChronoUnit.DAYS.between(date1.toInstant(), date2.toInstant()));
                cost = parking.getCm_price() + parking.getCn_price() * (date1.getDate() - date2.getDate());
            }
        }
        bill.setLicense_vehicle(vehicleInParking.getLicense_vehicle());
        bill.setEntry_time(vehicleInParking.getEntry_time());
        bill.setOut_time(currenTime1);
        bill.setParking_name(vehicleInParking.getParking_name());
        bill.setCost(cost);
        bill.setType(type_vehicle);
        billService.saveBill(bill);
        vehicleInParkingService.deleteVehicleInParking(vehicleInParking);
        return ResponseEntity.ok(bill);
}


//    @PostMapping("/add_month_ticket")
//    public ResponseEntity<?> addMonthTicket(@RequestBody RequestTicket requestTicket, @RequestHeader("Authorization") String jwt) {
//        Claims claims = JwtUtil.decodeToken(jwt.replaceFirst("Bearer ", ""));
//        String decodedRole = claims.get("role", String.class);
//        if (decodedRole.equals("employee")) {
////        LocalDateTime time = LocalDateTime.now();
//            MonthTicket monthTicket = new MonthTicket();
//            monthTicket.setId_card(requestTicket.getId_card());
//            monthTicket.setLicense_vehicle(requestTicket.getLicense_vehicle());
//            monthTicket.setType(requestTicket.getType());
//            monthTicket.setUsername(requestTicket.getUsername());
//            monthTicket.setParking_name(requestTicket.getParking_name());
//            monthTicket.setDuration(requestTicket.getDuration());
//            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
//            monthTicket.setTime_register(currentTimestamp);
//            Parking parking = parkingService.getParkingByParking_name(requestTicket.getParking_name());
//            if (requestTicket.getType().toLowerCase().equals("car")) {
//                monthTicket.setCost(parking.getCar_month());
//            } else {
//                monthTicket.setCost(parking.getMotor_month());
//            }
////        MonthTicket monthTicket1 = monthTicket;
//            Owner owner = new Owner(monthTicket.getLicense_vehicle(), monthTicket.getUsername());
//            monthTicketService.saveMonthTicket(monthTicket);
//            ownerService.saveOwner(owner);
//            return ResponseEntity.ok(gson.toJson(monthTicket));
//        } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need employee role!");
//    }

        @PostMapping("/add_month_ticket")
    public ResponseEntity<?> addMonthTicket(@RequestBody RequestTicket requestTicket) {
//        Claims claims = JwtUtil.decodeToken(jwt.replaceFirst("Bearer ", ""));
//        String decodedRole = claims.get("role", String.class);
//        if (decodedRole.equals("employee")) {
//        LocalDateTime time = LocalDateTime.now();
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
            if (requestTicket.getType().toLowerCase().equals("car")) {
                monthTicket.setCost(parking.getCar_month());
            } else {
                monthTicket.setCost(parking.getMotor_month());
            }
//        MonthTicket monthTicket1 = monthTicket;
            Owner owner = new Owner(monthTicket.getLicense_vehicle(), monthTicket.getUsername());
            monthTicketService.saveMonthTicket(monthTicket);
            ownerService.saveOwner(owner);
            return ResponseEntity.ok(gson.toJson(monthTicket));
//        } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need employee role!");
    }

//    @GetMapping ("/check_month_ticket")
//    public ResponseEntity<?> checkMonthTicket(@RequestBody RequestTicket requestTicket, @RequestHeader("Authorization") String jwt) {
//        Claims claims = JwtUtil.decodeToken(jwt.replaceFirst("Bearer ", ""));
//        String decodedRole = claims.get("role", String.class);
//        if (decodedRole.equals("employee")) {
//            Parking parking = parkingService.getParkingByParking_name(requestTicket.getParking_name());
//            if (requestTicket.getType().equals("car")) {
//                return ResponseEntity.ok(gson.toJson(parking.getCar_month()));
//            } else {
//                return ResponseEntity.ok(gson.toJson(parking.getMotor_month()));
//            }
//        } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need employee role!");
//    }
@GetMapping ("/check_month_ticket")
public ResponseEntity<?> checkMonthTicket(@RequestParam String parking_name, @RequestParam String type, @RequestHeader("Authorization") String jwt) {
        System.out.println(jwt);
        Claims claims = JwtUtil.decodeToken(jwt.replaceFirst("Bearer ", ""));
        String decodedRole = claims.get("role", String.class);
        if (decodedRole.equals("employee")) {
        ParkingCostDto parkingCostDto = new ParkingCostDto();
        Parking parking = parkingService.getParkingByParking_name(parking_name);
        if (type.toLowerCase().equals("car")) {
            parkingCostDto.setCost(parking.getCar_month());
            return ResponseEntity.ok(parkingCostDto);
        } else {
            parkingCostDto.setCost(parking.getMotor_month());
            return ResponseEntity.ok(parkingCostDto);
        }
    }
        else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need employee role!");
    }


    @GetMapping ("/find_ticket")
    public ResponseEntity<?> findTicket(@RequestParam String id_card) {
        MonthTicket monthTicket = monthTicketService.getTicket(id_card);
        if(monthTicket!=null){
            return  ResponseEntity.ok(gson.toJson(monthTicket));
        }
        else return null;
    }
//@GetMapping ("/check_month_ticket")
//public ResponseEntity<?> checkMonthTicket(@RequestParam String parking_name, @RequestParam String type) {
//
//        ParkingCostDto parkingCostDto = new ParkingCostDto();
//        Parking parking = parkingService.getParkingByParking_name(parking_name);
//        if (type.equals("car")) {
//            parkingCostDto.setCost(parking.getCar_month());
//            return ResponseEntity.ok(parkingCostDto);
//        } else {
//            parkingCostDto.setCost(parking.getMotor_month());
//            return ResponseEntity.ok(parkingCostDto);
//        }
//
//}



}
