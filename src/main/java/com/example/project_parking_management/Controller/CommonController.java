package com.example.project_parking_management.Controller;

import com.example.project_parking_management.Auth.JwtUtil;
import com.example.project_parking_management.Calculate.Distance;
import com.example.project_parking_management.Dto.ParkingDto;
import com.example.project_parking_management.Entity.Parking;
import com.example.project_parking_management.Entity.VehicleInParking;
import com.example.project_parking_management.Service.ParkingService;
//import com.example.project_parking_management.Service.VehicleInParkingService;
import com.example.project_parking_management.Service.VehicleInParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@ResponseBody
@CrossOrigin
public class CommonController {
    @Autowired
    VehicleInParkingService vehicleInParkingService;
    @Autowired
    ParkingService parkingService;
    @GetMapping("/get-all-parking")
    public ResponseEntity<?> getAllParking() throws SQLException, ClassNotFoundException {
        List<Parking> parkings = parkingService.getAllParking();
        return ResponseEntity.ok(parkings);
    }
    @GetMapping("/get-sort-distance")
    public ResponseEntity<?> getSortDistance(@RequestParam double longtitude, @RequestParam double latitude) throws SQLException, ClassNotFoundException {
        List<Parking> parkings = parkingService.getAllParking();
        int count = parkings.size();
        ParkingDto[] parkingDtos = new ParkingDto[count];
        int a=0;
        for(Parking parking:parkings){
            ParkingDto parkingDto = new ParkingDto();
            double distance = Distance.getDistance(longtitude, latitude, parking.getLongitude(), parking.getLatitude());
            List<VehicleInParking> vehicleInParkings
                    = vehicleInParkingService.getVehicleInParking();
            int cnt_motor = 0;
            int cnt_car =0;
            for(VehicleInParking vehicle:vehicleInParkings){

                if((vehicle.getParking_name()).equals(parking.getParking_name())&&vehicle.getType().equals("motor")){
                    cnt_motor++;
                }
                if((vehicle.getParking_name()).equals(parking.getParking_name())&&vehicle.getType().equals("car")){
                    cnt_car++;
                }
            }
            parkingDto.setParking_name(parking.getParking_name());
            parkingDto.setParking_address(parking.getParking_name());
            parkingDto.setDistance(distance);
            parkingDto.setAvailable_motor(parking.getCapacity_motor()-cnt_motor);
            parkingDto.setAvailable_car(parking.getCapacity_car()-cnt_car);
            parkingDtos[a] = parkingDto;
            a++;
        }
        for(int i=0; i<count-1;i++){
            for(int j=i+1;j<count;j++){
                if(parkingDtos[i].getDistance()>parkingDtos[j].getDistance()){
                    ParkingDto temp = new ParkingDto();
                    temp = parkingDtos[j];
                    parkingDtos[j] = parkingDtos[i];
                    parkingDtos[i] = temp;
                }
            }
        }
        return ResponseEntity.ok(parkingDtos);
    }
}
