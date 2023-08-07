package com.example.project_parking_management.Controller;

import com.example.project_parking_management.Auth.JwtUtil;
import com.example.project_parking_management.Calculate.Distance;
import com.example.project_parking_management.Dto.ParkingDto;
import com.example.project_parking_management.Dto.ParkingEmDto;
import com.example.project_parking_management.Encode.PasswordUtil;
import com.example.project_parking_management.Entity.Account;
import com.example.project_parking_management.Entity.Management;
import com.example.project_parking_management.Entity.Parking;
import com.example.project_parking_management.Entity.VehicleInParking;
import com.example.project_parking_management.Json.RequestLogin;
import com.example.project_parking_management.Json.ResponseLogin;
import com.example.project_parking_management.Service.AccountService;
import com.example.project_parking_management.Service.ManagementService;
import com.example.project_parking_management.Service.ParkingService;
//import com.example.project_parking_management.Service.VehicleInParkingService;
import com.example.project_parking_management.Service.VehicleInParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@ResponseBody
@CrossOrigin
public class CommonController {
    @Autowired
    ManagementService managementService;
    @Autowired
    AccountService accountService;
    @Autowired
    VehicleInParkingService vehicleInParkingService;
    @Autowired
    ParkingService parkingService;

    @GetMapping("/get-all-parking")
    public ResponseEntity<?> getAllParking() throws SQLException, ClassNotFoundException {
        List<Parking> parkings = parkingService.getAllParking();
        List<String> parkingNames = new ArrayList<>();
        List<ParkingDto> parkingDtos = new ArrayList<>();
        for (Parking parking : parkings) {
            ParkingDto parkingDto = new ParkingDto();
            parkingDto.setParking_name(parking.getParking_name());
            parkingDto.setParking_address(parking.getParking_address());
            parkingDto.setCapacity_motor(parking.getCapacity_motor());
            parkingDto.setCapacity_car(parking.getCapacity_car());
            parkingDto.setLongtitude(parking.getLongitude());
            parkingDto.setLatitude(parking.getLatitude());
            parkingDto.setDistance(0);
            parkingDtos.add(parkingDto);
        }
        return ResponseEntity.ok(parkingDtos);
    }

    @GetMapping("/get_info_all_parking")
    public ResponseEntity<?> getInfoAllParking() throws SQLException, ClassNotFoundException {
        List<Parking> parkings = parkingService.getAllParking();
        return ResponseEntity.ok(parkings);
    }

    @GetMapping("/get-all-parking-name")
    public ResponseEntity<?> getAllParkingName() throws SQLException, ClassNotFoundException {
        List<Parking> parkings = parkingService.getAllParking();
        List<String> parkingNames = new ArrayList<>();
//        List<ParkingDto> parkingDtos = new ArrayList<>();
        for (Parking parking : parkings) {
//            ParkingDto parkingDto = new ParkingDto();
//            parkingDto.setParking_name(parking.getParking_name());
//            parkingDto.setParking_address(parking.getParking_address());
//            parkingDto.setCapacity_motor(parking.getCapacity_motor());
//            parkingDto.setCapacity_car(parking.getCapacity_car());
//            parkingDto.setLongtitude(parking.getLongitude());
//            parkingDto.setLatitude(parking.getLatitude());
//            parkingDto.setDistance(0);
            parkingNames.add(parking.getParking_name());
//            parkingDtos.add(parkingDto);
        }
        return ResponseEntity.ok(parkingNames);
    }

    @GetMapping("/get-sort-distance")
    public ResponseEntity<?> getSortDistance(@RequestParam double longtitude, @RequestParam double latitude) throws SQLException, ClassNotFoundException {
        List<Parking> parkings = parkingService.getAllParking();
        int count = parkings.size();
        ParkingDto[] parkingDtos = new ParkingDto[count];
        int a = 0;
        for (Parking parking : parkings) {
            ParkingDto parkingDto = new ParkingDto();
            double distance = Distance.getDistance(longtitude, latitude, parking.getLongitude(), parking.getLatitude());
            List<VehicleInParking> vehicleInParkings
                    = vehicleInParkingService.getVehicleInParking();
            int cnt_motor = 0;
            int cnt_car = 0;
            for (VehicleInParking vehicle : vehicleInParkings) {

                if ((vehicle.getParking_name()).equals(parking.getParking_name()) && vehicle.getType().equals("motor")) {
                    cnt_motor++;
                }
                if ((vehicle.getParking_name()).equals(parking.getParking_name()) && vehicle.getType().equals("car")) {
                    cnt_car++;
                }
            }
            distance *= 1.2;
            parkingDto.setParking_name(parking.getParking_name());
            parkingDto.setParking_address(parking.getParking_address());
            parkingDto.setDistance(Double.parseDouble(String.format("%.1f", distance)));
//            System.out.println(parking.getCapacity_motor());
//            System.out.println(cnt_motor);
//            System.out.println(parking.getCapacity_car());
//            System.out.println(cnt_car);
//            System.out.println(parking.getCapacity_motor() - cnt_motor);
//            System.out.println(parking.getCapacity_car() - cnt_car);
//            System.out.println(parking.getParking_name());
            parkingDto.setCapacity_motor(parking.getCapacity_motor() - cnt_motor);
            parkingDto.setCapacity_car(parking.getCapacity_car() - cnt_car);
            parkingDto.setLongtitude(parking.getLongitude());
            parkingDto.setLatitude(parking.getLatitude());
            parkingDtos[a] = parkingDto;
            a++;
        }
        for (int i = 0; i < count - 1; i++) {
            for (int j = i + 1; j < count; j++) {
                if (parkingDtos[i].getDistance() > parkingDtos[j].getDistance()) {
                    ParkingDto temp = new ParkingDto();
                    temp = parkingDtos[j];
                    parkingDtos[j] = parkingDtos[i];
                    parkingDtos[i] = temp;
                }
            }
        }
        return ResponseEntity.ok(parkingDtos);
    }

    @GetMapping("/get-info-parking")
    public ResponseEntity<?> getInfoParking(@RequestParam String parking_name) throws SQLException, ClassNotFoundException {
        Parking parking = parkingService.getParkingByParking_name(parking_name);
        ParkingEmDto parkingDto = new ParkingEmDto();
        List<VehicleInParking> vehicleInParkings
                = vehicleInParkingService.getVehicleInParking();
        int cnt_motor = 0;
        int cnt_car = 0;
        for (VehicleInParking vehicle : vehicleInParkings) {
            if ((vehicle.getParking_name()).equals(parking.getParking_name()) && vehicle.getType().toLowerCase().equals("motor")) {
                cnt_motor++;
            }
            if ((vehicle.getParking_name()).equals(parking.getParking_name()) && vehicle.getType().toLowerCase().equals("car")) {
                cnt_car++;
            }
        }
        parkingDto.setParking_name(parking.getParking_name());
        parkingDto.setParking_address(parking.getParking_address());
        parkingDto.setDistance(0L);
        parkingDto.setEmpty_motor(parking.getCapacity_motor() - cnt_motor);
        parkingDto.setEmpty_car(parking.getCapacity_car() - cnt_car);
        parkingDto.setCapacity_motor(parking.getCapacity_motor());
        parkingDto.setCapacity_car(parking.getCapacity_car());
        parkingDto.setCar_month(parking.getCar_month());
        parkingDto.setMotor_month(parking.getMotor_month());
        parkingDto.setLongtitude(parking.getLongitude());
        parkingDto.setLatitude(parking.getLatitude());
        return ResponseEntity.ok(parkingDto);
}

    //    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) throws SQLException, ClassNotFoundException {
//            Account account = accountService.findByUserName(username);
//            if (account != null && PasswordUtil.checkPassword(password, account.getPassword())) {
//                if(account.getRole().equals("employee")){
//                    String role = "employee";
//                    String accessToken = JwtUtil.generateToken(username, password, "employee");
//                    Management management = managementService.findByUsername(username);
//                    String parkingName = management.getParking_name();
//                    ResponseLogin responseLogin = new ResponseLogin(accessToken, role, parkingName);
//                  return ResponseEntity.ok(responseLogin);
//                }
//                else if (account.getRole().equals("manager")){
//                    String role = "manager";
//                    String accessToken = JwtUtil.generateToken(username, password, "manager");
//                    String parkingName = "";
//                    ResponseLogin responseLogin = new ResponseLogin(accessToken, role, parkingName);
//                    return ResponseEntity.ok(responseLogin);
//                }
//                else return ResponseEntity.notFound().build();
//            }
//            else return ResponseEntity.notFound().build();
//    }
    @PostMapping("/login")
    public ResponseEntity<?> login1(@RequestBody RequestLogin requestbody) throws SQLException, ClassNotFoundException {
        String username = requestbody.getUsername();
        String password = requestbody.getPassword();
        Account account = accountService.findByUserName(username);
        System.out.println(account);
        if (account != null && PasswordUtil.checkPassword(password, account.getPassword())) {
            if (account.getRole().equals("employee")) {
                System.out.println("e");
                String role = "employee";
                String accessToken = JwtUtil.generateToken(username, password, "employee");
                Management management = managementService.findByUsername(username);
                String parkingName = management.getParking_name();
                ResponseLogin responseLogin = new ResponseLogin(accessToken, 1, parkingName);
                return ResponseEntity.ok(responseLogin);
            } else if (account.getRole().equals("manager")) {
                System.out.println("m");
                String role = "manager";
                String accessToken = JwtUtil.generateToken(username, password, "manager");
                String parkingName = "";
                ResponseLogin responseLogin = new ResponseLogin(accessToken, 0, parkingName);
                return ResponseEntity.ok(responseLogin);
            } else return ResponseEntity.notFound().build();
        } else return ResponseEntity.notFound().build();
    }

}
