package com.example.project_parking_management.Service;

import com.example.project_parking_management.Entity.Parking;
import com.example.project_parking_management.Entity.VehicleInParking;
import com.example.project_parking_management.Repository.ParkingRepository;
import com.example.project_parking_management.Repository.VehicleInParkingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleInParkingService {
    private final VehicleInParkingRepository vehicleInParkingRepository;

    public VehicleInParkingService(VehicleInParkingRepository vehicleInParkingRepository) {
        this.vehicleInParkingRepository = vehicleInParkingRepository;
    }
    public VehicleInParking saveVehicleInParking(VehicleInParking vehicleInParking){
        return vehicleInParkingRepository.save(vehicleInParking);
    }
    public List<VehicleInParking> getVehicleInParking() {

        return vehicleInParkingRepository.findAll();
    }
    public VehicleInParking getVehicleById_card(String id_card) {

        return vehicleInParkingRepository.findByIdCard(id_card);
    }
    public void deleteVehicleInParking(VehicleInParking vehicleInParking){
        vehicleInParkingRepository.delete(vehicleInParking);
    }
}
