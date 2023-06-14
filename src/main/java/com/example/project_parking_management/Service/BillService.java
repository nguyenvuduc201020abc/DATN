package com.example.project_parking_management.Service;

import com.example.project_parking_management.Entity.Account;
import com.example.project_parking_management.Entity.Bill;
import com.example.project_parking_management.Repository.BillRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillService {
    private final BillRepository billRepository;

    public BillService(BillRepository billRepository) {
        this.billRepository = billRepository;
    }
    public Bill saveBill(Bill bill) {
        return billRepository.save(bill);
    }
    public List<Bill> getAllBill(){
        return billRepository.findAll();
    }
    public List<Bill> getBillByParkingName(String parking_name){
        return billRepository.findByParkingName(parking_name);
    }
}
