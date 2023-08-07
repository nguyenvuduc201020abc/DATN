package com.example.project_parking_management.Service;

import com.example.project_parking_management.Entity.Bill;
import com.example.project_parking_management.Entity.EntryVehicle;
import com.example.project_parking_management.Repository.EntryVehicleRepository;
import org.springframework.stereotype.Service;

@Service
public class EntryVehicleService {
    private final EntryVehicleRepository entryVehicleRepository;

    public EntryVehicleService(EntryVehicleRepository entryVehicleRepository) {
        this.entryVehicleRepository = entryVehicleRepository;
    }

    public EntryVehicle saveEntryVehicle(EntryVehicle entryVehicle) {
        return entryVehicleRepository.save(entryVehicle);
    }

}
