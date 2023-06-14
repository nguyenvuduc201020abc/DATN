package com.example.project_parking_management.Service;

import com.example.project_parking_management.Entity.Account;
import com.example.project_parking_management.Entity.Parking;
import com.example.project_parking_management.Repository.ParkingRepository;
import org.springframework.stereotype.Service;

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
}
