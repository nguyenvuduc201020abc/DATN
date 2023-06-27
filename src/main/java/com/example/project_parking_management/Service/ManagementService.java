package com.example.project_parking_management.Service;

import com.example.project_parking_management.Entity.Management;
import com.example.project_parking_management.Repository.ManagementRepository;
import org.springframework.stereotype.Service;

@Service
public class ManagementService  {
    private final ManagementRepository managementRepository;

    public ManagementService(ManagementRepository managementRepository) {
        this.managementRepository = managementRepository;
    }
    public Management saveManagement(Management management){
        return managementRepository.save(management);
    }
    public void deleteManagement(Management management){
        managementRepository.delete(management);
    }

    public Management findByUsername(String username){
        return managementRepository.findByUsername(username);
    }
}
