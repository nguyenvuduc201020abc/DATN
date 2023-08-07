package com.example.project_parking_management.Service;

import com.example.project_parking_management.Entity.Management;
import com.example.project_parking_management.Repository.ManagementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ManagementService {
    private final ManagementRepository managementRepository;

    public ManagementService(ManagementRepository managementRepository) {
        this.managementRepository = managementRepository;
    }

    public Management saveManagement(Management management) {
        return managementRepository.save(management);
    }

    public void deleteManagement(Management management) {
        managementRepository.delete(management);
    }

    public Management findByUsername(String username) {
        return managementRepository.findByUsername(username);
    }

//    @Transactional
//    public void deleteManagement(String username) {
//        managementRepository.deleteById(username);
//    }

    @Transactional
    public void updateManagement(String username, String parking_name) {
        Management management = managementRepository.findByUsername(username);
        System.out.println(management);
        if (management != null) {
            management.setUsername(username);
            management.setParking_name(parking_name);
            managementRepository.save(management);
        }
    }
}
