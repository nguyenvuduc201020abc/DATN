package com.example.project_parking_management.Service;

import com.example.project_parking_management.Entity.MonthTicket;
import com.example.project_parking_management.Entity.Owner;
import com.example.project_parking_management.Repository.OwnerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OwnerService {
    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }
    public Owner saveOwner(Owner owner){
        return ownerRepository.save(owner);
    }
    public List<Owner> getListOwner(String username){
        return ownerRepository.findByUsername(username);
    }


}
