package com.example.project_parking_management.Service;

import com.example.project_parking_management.Entity.Account;
import com.example.project_parking_management.Entity.Management;
import com.example.project_parking_management.Entity.Parking;
import com.example.project_parking_management.Repository.ParkingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ParkingService {
    private final ParkingRepository parkingRepository;

    public ParkingService(ParkingRepository parkingRepository) {
        this.parkingRepository = parkingRepository;
    }
    public Parking saveParking(Parking parking) {

        return parkingRepository.save(parking);
    }
    public List<Parking> getAllParking() {
        return parkingRepository.findAll();
    }
    public Parking getParkingByParking_name(String parking_name){
        return parkingRepository.findByParkingName(parking_name);
    }
    @Transactional
    public void updateParking(String parking_name, String parking_address, Long mm_price, Long mn_price, Long cm_price, Long cn_price, Long car_month, Long motor_month, Double longtitude, Double latitude, int capacity_motor, int capacity_car) {
        Parking parking = parkingRepository.findByParkingName(parking_name);
        System.out.println(parking);
        if (parking != null) {
            parking.setParking_name(parking_name);
            parking.setParking_address(parking_address);
            parking.setMm_price(mm_price);
            parking.setMn_price(mn_price);
            parking.setCm_price(cm_price);
            parking.setCn_price(cn_price);
            parking.setCar_month(car_month);
            parking.setMotor_month(motor_month);
            parking.setLongitude(longtitude);
            parking.setLatitude(latitude);
            parking.setCapacity_car(capacity_car);
            parking.setCapacity_motor(capacity_motor);
            parkingRepository.save(parking);
        }
    }
}
