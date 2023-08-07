package com.example.project_parking_management.Controller;

import com.example.project_parking_management.Auth.JwtUtil;
import com.example.project_parking_management.Dto.EmployeeDto;
import com.example.project_parking_management.Encode.PasswordUtil;
import com.example.project_parking_management.Entity.*;
import com.example.project_parking_management.Json.InfoDetailParking;
import com.example.project_parking_management.Json.InfoHomePage;
import com.example.project_parking_management.Json.StatisticRevenue;
import com.example.project_parking_management.Service.*;
//import com.example.project_parking_management.Service.MonthTicketService;
//import com.example.project_parking_management.Service.ParkingService;
import com.example.project_parking_management.Statistic.RevenueParking;
import com.example.project_parking_management.Statistic.RevenueStatisticAll;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@ResponseBody
@CrossOrigin
public class ManagerController {
    @Autowired
    EntryVehicleService entryVehicleService;
    @Autowired
    ManagementService managementService;
    @Autowired
    MonthTicketService monthTicketService;
    private PasswordUtil passwordUtil;
    @Autowired
    AccountService accountService;
    @Autowired
    ParkingService parkingService;
    @Autowired
    BillService billService;
    @Autowired
    VehicleInParkingService vehicleInParkingService;
    Gson gson = new Gson();
//    @PostMapping("/login-manager")
//    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) throws SQLException, ClassNotFoundException {
//        if (accountService.checkLoginManage(username, password)){
//            String token = JwtUtil.generateToken(username, password, "manager");
//            return ResponseEntity.ok(token);
//        }
//        else return ResponseEntity.notFound().build();
//    }

//    @PostMapping("/add-parking")
//    public ResponseEntity<?> addParking(@RequestParam String parking_name, @RequestParam String parking_address, @RequestParam Long mm_price, @RequestParam Long mn_price, @RequestParam Long cm_price, @RequestParam Long cn_price, @RequestParam Long car_month, @RequestParam Long motor_month, @RequestParam Double longitude, @RequestParam Double latitude, @RequestParam int capacity_motor, @RequestParam int capacity_car) {
////        Claims claims = JwtUtil.decodeToken(jwt.replaceFirst("Bearer ", ""));
////        String decodedRole = claims.get("role", String.class);
////        if (decodedRole.equals("manager")) {
//            Parking parking = new Parking(parking_name, parking_address, mm_price, mn_price, cm_price, cn_price, car_month, motor_month, longitude, latitude, capacity_motor, capacity_car);
//            parkingService.saveParking(parking);
//            return ResponseEntity.ok(parking);
////        } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need admin role!");
//    }

    @PostMapping("/add-parking")
    public ResponseEntity<?> addParking( @RequestBody Parking parking1) {
//        Claims claims = JwtUtil.decodeToken(jwt.replaceFirst("Bearer ", ""));
//        String decodedRole = claims.get("role", String.class);
//        if (decodedRole.equals("manager")) {
        System.out.println(parking1.getParking_name());
        Parking parking = new Parking(parking1.getParking_name(), parking1.getParking_address(), parking1.getMm_price(), parking1.getMn_price(), parking1.getCm_price(), parking1.getCn_price(), parking1.getCar_month(), parking1.getMotor_month(), parking1.getLongitude(), parking1.getLatitude(), parking1.getCapacity_motor(), parking1.getCapacity_car());
        parkingService.saveParking(parking);
        return ResponseEntity.ok(parking);
//        } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need admin role!");
    }

//    @PostMapping("/add-employee")
//    public ResponseEntity<?> addEmployee(@RequestParam String parking_name, @RequestParam String username, @RequestParam String password, @RequestHeader("Authorization") String jwt) {
//        Claims claims = JwtUtil.decodeToken(jwt.replaceFirst("Bearer ", ""));
//        String decodedRole = claims.get("role", String.class);
//        if (decodedRole.equals("manager")) {
//            Management management = new Management(username, parking_name);
//            managementService.saveManagement(management);
//            Account account = new Account(username, passwordUtil.hashPassword(password), "employee");
//            accountService.saveAccount(account);
//            return ResponseEntity.ok(management);
//        } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need admin role!");
//    }

    @PostMapping("/add-employee")
    public ResponseEntity<?> addEmployee(@RequestParam String parking_name, @RequestParam String username, @RequestParam String password) {
//        Claims claims = JwtUtil.decodeToken(jwt.replaceFirst("Bearer ", ""));
//        String decodedRole = claims.get("role", String.class);
//        if (decodedRole.equals("manager")) {
            Management management = new Management(username, parking_name);
            managementService.saveManagement(management);
            Account account = new Account(username, passwordUtil.hashPassword(password), "employee");
            accountService.saveAccount(account);
            return ResponseEntity.ok(management);
//        } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need admin role!");
    }

    @GetMapping("/get_employee_info")
    public ResponseEntity<?> getEmployeeInfo() {
        List<Account> accounts = accountService.getAll();
        List<EmployeeDto> employeeDtos = new ArrayList<>();
        for(Account account:accounts){
            if(account.getRole().equals("employee")){
            String username = account.getUsername();
            Management management = managementService.findByUsername(username);
            String parking_name = management.getParking_name();
            String password = account.getPassword();
            EmployeeDto employeeDto = new EmployeeDto(username,password,parking_name);
            employeeDtos.add(employeeDto);}
        }
        return ResponseEntity.ok(gson.toJson(employeeDtos));
    }

//    @PostMapping("/delete-employee")
//    public ResponseEntity<?> deleteEmployee(@RequestParam String username, @RequestHeader("Authorization") String jwt) {
//        Claims claims = JwtUtil.decodeToken(jwt.replaceFirst("Bearer ", ""));
//        String decodedRole = claims.get("role", String.class);
//        if (decodedRole.equals("manager")) {
//            Account account = accountService.findByUserName(username);
//            Management management = managementService.findByUsername(username);
//            managementService.deleteManagement(management);
//            accountService.deleteAccount(account);
//            return ResponseEntity.ok(management);
//        } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need admin role!");
//    }
    @DeleteMapping ("/delete-employee")
    public ResponseEntity<?> deleteEmployee(@RequestParam String username) {
            Account account = accountService.findByUserName(username);
            Management management = managementService.findByUsername(username);
            managementService.deleteManagement(management);
            accountService.deleteAccount(account);
            return ResponseEntity.ok(management);
    }
    @PutMapping ("/update-employee")
    public ResponseEntity<?> putEmployee(@RequestBody Management management) {
            managementService.updateManagement(management.getUsername() , management.getParking_name());
            return ResponseEntity.ok(management);
    }

    @PutMapping ("/update-parking")
    public ResponseEntity<?> putParking(@RequestBody Parking parking) {
        parkingService.updateParking(parking.getParking_name(), parking.getParking_address(), parking.getMm_price(), parking.getMn_price(), parking.getCm_price(), parking.getCn_price(), parking.getCar_month(), parking.getMotor_month(), parking.getLongitude(), parking.getLatitude(), parking.getCapacity_motor(), parking.getCapacity_car());
        return ResponseEntity.ok(parking);
    }

    @PostMapping("/signup-manager")
    public String signup(@RequestParam String username, @RequestParam String password) throws SQLException, ClassNotFoundException {
        final String role = "manager";
        Account accounts = new Account(username, passwordUtil.hashPassword(password), role);
        accountService.saveAccount(accounts);
        return accounts.toString();
    }

    @GetMapping("/statistic-revenue")
    public ResponseEntity<?> statisticRevenue(@RequestParam String date3, @RequestParam String date4, @RequestHeader("Authorization") String jwt) throws SQLException, ClassNotFoundException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(date3 + " 00:00:00", formatter);
        Timestamp date1 = Timestamp.valueOf(localDateTime);

        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime1 = LocalDateTime.parse(date4 + " 00:00:00", formatter1);
        Timestamp date2 = Timestamp.valueOf(localDateTime1);


        Claims claims = JwtUtil.decodeToken(jwt.replaceFirst("Bearer ", ""));
        String decodedRole = claims.get("role", String.class);
        RevenueStatisticAll revenueStatisticAll = new RevenueStatisticAll();
        revenueStatisticAll.setBeginTime(date1);
        revenueStatisticAll.setEndTime(date2);
        Long revenueMotor = 0L;
        Long revenueCar = 0L;
        Long revenueMotorMonth = 0L;
        Long revenueCarMonth = 0L;
        System.out.println(decodedRole);
        if (decodedRole.equals("manager")) {
            List<Bill> bills = billService.getAllBill();
            for (Bill bill : bills) {
                if (bill.getEntry_time().after(date1) && bill.getEntry_time().before(date2) && bill.getType().toLowerCase().equals("motor")) {
                    revenueMotor += bill.getCost();
                }
                if (bill.getEntry_time().after(date1) && bill.getEntry_time().before(date2) && bill.getType().toLowerCase().equals("car")) {
                    revenueCar += bill.getCost();
                }
            }
            List<MonthTicket> monthTickets = monthTicketService.getAllMonthTicket();
            for (MonthTicket monthTicket : monthTickets) {
                if (monthTicket.getTime_register().after(date1) && monthTicket.getTime_register().before(date2) && monthTicket.getType().toLowerCase().equals("motor")) {
                    revenueMotorMonth += monthTicket.getCost();
                }
                if (monthTicket.getTime_register().after(date1) && monthTicket.getTime_register().before(date2) && monthTicket.getType().toLowerCase().equals("car")) {
                    revenueCarMonth += monthTicket.getCost();
                }
            }
            revenueStatisticAll.setRevenueCar(revenueCar);
            revenueStatisticAll.setRevenueMotor(revenueMotor);
            revenueStatisticAll.setRevenueCarMonth(revenueCarMonth);
            revenueStatisticAll.setRevenueMotorMonth(revenueMotorMonth);
            revenueStatisticAll.setRevenueCarAll(revenueCar + revenueCarMonth);
            revenueStatisticAll.setRevenueMotorAll(revenueMotor + revenueMotorMonth);
            revenueStatisticAll.setRevenueAll(revenueCar + revenueCarMonth + revenueMotor + revenueMotorMonth);
            return ResponseEntity.ok(revenueStatisticAll);
        } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need admin role!");
    }

    @GetMapping("/statistic_revenue_month")
    public ResponseEntity<?> statisticRevenueMonth(@RequestParam int month) throws SQLException, ClassNotFoundException {

        Long revenue = 0L;
//        if (decodedRole.equals("manager")) {
            List<Bill> bills = billService.getAllBill();
            for (Bill bill : bills) {
                Timestamp time = bill.getEntry_time();
                LocalDateTime dateTime = time.toLocalDateTime();
                Month month1 = dateTime.getMonth();
                int monthValue = month1.getValue();
                if(monthValue==month){
                    revenue+=bill.getCost();
                }
            }
            List<MonthTicket> monthTickets = monthTicketService.getAllMonthTicket();
            for (MonthTicket monthTicket : monthTickets) {
                Timestamp time = monthTicket.getTime_register();
                LocalDateTime dateTime = time.toLocalDateTime();
                Month month1 = dateTime.getMonth();
                int monthValue = month1.getValue();
                if(monthValue==month){
                    revenue+=monthTicket.getCost();
                }
            }
            return ResponseEntity.ok(revenue);
//        } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need admin role!");
    }

    @GetMapping("/statistic-revenue-parking")
    public ResponseEntity<?> statisticRevenueParking(@RequestParam String parking_name, @RequestParam Timestamp date3, @RequestParam Timestamp date4, @RequestHeader("Authorization") String jwt) throws SQLException, ClassNotFoundException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(date3 + " 00:00:00", formatter);
        Timestamp date1 = Timestamp.valueOf(localDateTime);

        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime1 = LocalDateTime.parse(date4 + " 00:00:00", formatter1);
        Timestamp date2 = Timestamp.valueOf(localDateTime1);


        Claims claims = JwtUtil.decodeToken(jwt.replaceFirst("Bearer ", ""));
        String decodedRole = claims.get("role", String.class);
        RevenueParking revenueParking = new RevenueParking();
        revenueParking.setParking_name(parking_name);
        revenueParking.setBeginTime(date1);
        revenueParking.setEndTime(date2);
        Long revenueMotor = 0L;
        Long revenueCar = 0L;
        Long revenueMotorMonth = 0L;
        Long revenueCarMonth = 0L;
        if (decodedRole.equals("manager")) {
            List<Bill> bills = billService.getBillByParkingName(parking_name);
            for (Bill bill : bills) {
                if (bill.getEntry_time().after(date1) && bill.getEntry_time().before(date2) && bill.getType().toLowerCase().equals("motor") ) {
                    revenueMotor += bill.getCost();
                }
                if (bill.getEntry_time().after(date1) && bill.getEntry_time().before(date2) && bill.getType().toLowerCase().equals("car") ) {
                    revenueCar += bill.getCost();
                }
            }
            List<MonthTicket> monthTickets = monthTicketService.getMonthTicketByParkingName(parking_name);
            for (MonthTicket monthTicket : monthTickets) {
                if (monthTicket.getTime_register().after(date1) && monthTicket.getTime_register().before(date2) && monthTicket.getType().toLowerCase().equals("motor") ) {
                    revenueMotorMonth += monthTicket.getCost();
                }
                if (monthTicket.getTime_register().after(date1) && monthTicket.getTime_register().before(date2) && monthTicket.getType().toLowerCase().equals("car") ) {
                    revenueCarMonth += monthTicket.getCost();
                }
            }
            revenueParking.setRevenueCar(revenueCar);
            revenueParking.setRevenueMotor(revenueMotor);
            revenueParking.setRevenueCarMonth(revenueCarMonth);
            revenueParking.setRevenueMotorMonth(revenueMotorMonth);
            revenueParking.setRevenueCarAll(revenueCar + revenueCarMonth);
            revenueParking.setRevenueMotorAll(revenueMotor + revenueMotorMonth);
            revenueParking.setRevenueAll(revenueCar + revenueCarMonth + revenueMotor + revenueMotorMonth);
            return ResponseEntity.ok(revenueParking);
        } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need admin role!");
    }
    @GetMapping("/statistic-piechart")
    public ResponseEntity<?> statisticPiechart() throws SQLException, ClassNotFoundException {

//        Claims claims = JwtUtil.decodeToken(jwt.replaceFirst("Bearer ", ""));
//        String decodedRole = claims.get("role", String.class);

        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();
        String date3 = currentYear+"-0"+(currentMonth-1)+"-"+"01";
        String date4 = currentYear+"-0"+(currentMonth+1)+"-"+"01";


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(date3 + " 00:00:00", formatter);
        Timestamp date1 = Timestamp.valueOf(localDateTime);

        LocalDateTime localDateTime1 = LocalDateTime.parse(date4 + " 00:00:00", formatter);
        Timestamp date2 = Timestamp.valueOf(localDateTime1);

        List<StatisticRevenue> statisticRevenues = new ArrayList<>();
        List<Parking> parkings = parkingService.getAllParking();
        for(Parking parking:parkings) {
        RevenueParking revenueParking = new RevenueParking();
        revenueParking.setParking_name(parking.getParking_name());
        revenueParking.setBeginTime(date1);
        revenueParking.setEndTime(date2);
        Long revenueMotor = 0L;
        Long revenueCar = 0L;
        Long revenueMotorMonth = 0L;
        Long revenueCarMonth = 0L;
            List<Bill> bills = billService.getBillByParkingName(parking.getParking_name());
            for (Bill bill : bills) {
                System.out.println(parking.getParking_name());
                if(parking.getParking_name().equals("Bach Khoa Parking")) System.out.println(bill);
                if (bill.getEntry_time().after(date1) && bill.getEntry_time().before(date2) && bill.getType().toLowerCase().equals("motor") ) {

                    revenueMotor += bill.getCost();
                }
                if (bill.getEntry_time().after(date1) && bill.getEntry_time().before(date2) && bill.getType().toLowerCase().equals("car") ) {
                    revenueCar += bill.getCost();
                }
            }
            List<MonthTicket> monthTickets = monthTicketService.getMonthTicketByParkingName(parking.getParking_name());
            for (MonthTicket monthTicket : monthTickets) {
                if (monthTicket.getTime_register().after(date1) && monthTicket.getTime_register().before(date2) && monthTicket.getType().toLowerCase().equals("motor") ) {
                    revenueMotorMonth += monthTicket.getCost();
                }
                if (monthTicket.getTime_register().after(date1) && monthTicket.getTime_register().before(date2) && monthTicket.getType().toLowerCase().equals("car") ) {
                    revenueCarMonth += monthTicket.getCost();
                }
            }
            revenueParking.setRevenueAll(revenueCar + revenueCarMonth + revenueMotor + revenueMotorMonth);
            StatisticRevenue statisticRevenue = new StatisticRevenue();
            statisticRevenue.setRevenue(revenueCar + revenueCarMonth + revenueMotor + revenueMotorMonth);
            statisticRevenue.setParking_name(parking.getParking_name());
            statisticRevenues.add(statisticRevenue);
        }
            return ResponseEntity.ok(gson.toJson(statisticRevenues));
    }
    @GetMapping("/statistic_revenue_month_parking")
    public ResponseEntity<?> statisticRevenueMonthParking(@RequestParam int month, @RequestParam String parking_name) throws SQLException, ClassNotFoundException {

        Long revenue = 0L;
        Long revenueMonthTicket = 0L;
        Long revenueAll = 0L;
//        if (decodedRole.equals("manager")) {
        List<Bill> bills = billService.getAllBill();
        for (Bill bill : bills) {
            Timestamp time = bill.getEntry_time();
            LocalDateTime dateTime = time.toLocalDateTime();
            Month month1 = dateTime.getMonth();
            int monthValue = month1.getValue();
            if(monthValue==month && bill.getParking_name().equals(parking_name)){
                revenue+=bill.getCost();
            }
        }
        List<MonthTicket> monthTickets = monthTicketService.getAllMonthTicket();
        for (MonthTicket monthTicket : monthTickets) {
            Timestamp time = monthTicket.getTime_register();
            LocalDateTime dateTime = time.toLocalDateTime();
            Month month1 = dateTime.getMonth();
            int monthValue = month1.getValue();
            if(monthValue==month && monthTicket.getParking_name().equals(parking_name)){
                revenueMonthTicket+=monthTicket.getCost();
            }
        }
        revenueAll = revenueMonthTicket + revenue;
        return ResponseEntity.ok(revenueAll);
//        } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need admin role!");
    }
    @GetMapping("/statistic_revenue_day_parking")
    public ResponseEntity<?> statisticRevenueDayParking(@RequestParam int day, @RequestParam int month, @RequestParam String parking_name) throws SQLException, ClassNotFoundException {

        Long revenue = 0L;
        Long revenueMonthTicket = 0L;
        Long revenueAll = 0L;
//        if (decodedRole.equals("manager")) {
        List<Bill> bills = billService.getAllBill();
        for (Bill bill : bills) {
            Timestamp time = bill.getEntry_time();
            LocalDateTime dateTime = time.toLocalDateTime();
            Month month1 = dateTime.getMonth();
            int dayOfMonth = dateTime.getDayOfMonth();
            int monthValue = month1.getValue();
            if(monthValue==month&& dayOfMonth==day && bill.getParking_name().equals(parking_name)){
                revenue+=bill.getCost();
            }
        }
        List<MonthTicket> monthTickets = monthTicketService.getAllMonthTicket();
        for (MonthTicket monthTicket : monthTickets) {
            Timestamp time = monthTicket.getTime_register();
            LocalDateTime dateTime = time.toLocalDateTime();
            Month month1 = dateTime.getMonth();
            int monthValue = month1.getValue();
            int dayOfMonth = dateTime.getDayOfMonth();
//            if(monthTicket.getParking_name().equals(parking_name)){
//                System.out.println(monthValue);
//                System.out.println(month);
//                System.out.println(dayOfMonth);
//                System.out.println(day);
//                System.out.println();
//            }
            if(monthValue==month && dayOfMonth==day &&  monthTicket.getParking_name().equals(parking_name)){
                System.out.println(monthValue);
                System.out.println(month);
                System.out.println(dayOfMonth);
                System.out.println(day);
                System.out.println();
                revenueMonthTicket+=monthTicket.getCost();
            }
        }
        revenueAll = revenueMonthTicket + revenue;
        return ResponseEntity.ok(revenueAll);
//        } else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You need admin role!");
    }
    @GetMapping("/statisticHomePage")
    public ResponseEntity<?> statisticHomePage(@RequestParam int month) throws SQLException, ClassNotFoundException {
        List<Parking> parkings= parkingService.getAllParking();
        int numberParking = parkings.size();
        List<VehicleInParking> vehicleInParkings = vehicleInParkingService.getVehicleInParking();
        int numberVehiclesInParking = vehicleInParkings.size();
        Long revenue = 0L;
        Long revenueMonthTicket = 0L;
        Long revenueAll = 0L;
//        if (decodedRole.equals("manager")) {
        List<Bill> bills = billService.getAllBill();
        for (Bill bill : bills) {
            Timestamp time = bill.getEntry_time();
            LocalDateTime dateTime = time.toLocalDateTime();
            Month month1 = dateTime.getMonth();
            int monthValue = month1.getValue();
            if(monthValue==month){
                revenue+=bill.getCost();
            }
        }
        List<MonthTicket> monthTickets = monthTicketService.getAllMonthTicket();
        for (MonthTicket monthTicket : monthTickets) {
            Timestamp time = monthTicket.getTime_register();
            LocalDateTime dateTime = time.toLocalDateTime();
            Month month1 = dateTime.getMonth();
            int monthValue = month1.getValue();
            if(monthValue==month){
                revenueMonthTicket+=monthTicket.getCost();
            }
        }
        revenueAll = revenueMonthTicket + revenue;
        InfoHomePage infoHomePage = new InfoHomePage();
        infoHomePage.setRevenue(revenueAll);
        infoHomePage.setNumberParking(numberParking);
        infoHomePage.setNumberVehicleInParking(numberVehiclesInParking);
        return ResponseEntity.ok(infoHomePage);
    }
    @GetMapping("/statisticDetailParking")
    public ResponseEntity<?> statisticDetailParking(@RequestParam int month, @RequestParam String parking_name) throws SQLException, ClassNotFoundException {
        int numberVehicleSent = 0;
        List<VehicleInParking> vehicleInParkings = vehicleInParkingService.getVehicleInParking();
        int numberVehiclesInParking = 0;
        for(VehicleInParking vehicleInParking:vehicleInParkings){
            if(vehicleInParking.getParking_name().equals(parking_name)){
                numberVehiclesInParking++;
            }
        }
        Long revenue = 0L;
        Long revenueMonthTicket = 0L;
        Long revenueAll = 0L;
//        if (decodedRole.equals("manager")) {
        List<Bill> bills = billService.getAllBill();
        for (Bill bill : bills) {
            if(bill.getParking_name().equals(parking_name)) {
                Timestamp time = bill.getEntry_time();
                LocalDateTime dateTime = time.toLocalDateTime();
                Month month1 = dateTime.getMonth();
                int monthValue = month1.getValue();
                if (monthValue == month) {
                    revenue += bill.getCost();
                }
            }
        }
        List<EntryVehicle> entryVehicles = entryVehicleService.getEntryVehicle();
        for(EntryVehicle entryVehicle:entryVehicles){
            if(entryVehicle.getParking_name().equals(parking_name)){
                Timestamp time = entryVehicle.getEntry_time();
                LocalDateTime dateTime = time.toLocalDateTime();
                Month month1 = dateTime.getMonth();
                int monthValue = month1.getValue();
                if (monthValue == month) {
                    numberVehicleSent++;
                }
            }
        }
        List<MonthTicket> monthTickets = monthTicketService.getAllMonthTicket();
        for (MonthTicket monthTicket : monthTickets) {
            if(monthTicket.getParking_name().equals(parking_name)) {
                Timestamp time = monthTicket.getTime_register();
                LocalDateTime dateTime = time.toLocalDateTime();
                Month month1 = dateTime.getMonth();
                int monthValue = month1.getValue();
                if (monthValue == month) {
                    revenueMonthTicket += monthTicket.getCost();
                }
            }
        }
        revenueAll = revenueMonthTicket + revenue;
        InfoDetailParking infoDetailParking = new InfoDetailParking();
        infoDetailParking.setRevenue(revenueAll);
        infoDetailParking.setNumberVehicleSent(numberVehicleSent);
        infoDetailParking.setNumberVehicleInParking(numberVehiclesInParking);
        return ResponseEntity.ok(infoDetailParking);
    }
}
