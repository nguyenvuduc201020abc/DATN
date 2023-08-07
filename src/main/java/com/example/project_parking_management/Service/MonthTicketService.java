package com.example.project_parking_management.Service;

import com.example.project_parking_management.Entity.MonthTicket;
import com.example.project_parking_management.Repository.MonthTicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonthTicketService {
    private final MonthTicketRepository monthTicketRepository;

    public MonthTicketService(MonthTicketRepository monthTicketRepository) {
        this.monthTicketRepository = monthTicketRepository;
    }

    public MonthTicket saveMonthTicket(MonthTicket monthTicket) {
        return monthTicketRepository.save(monthTicket);
    }

    public List<MonthTicket> getListMonthTicket(String username) {
        return monthTicketRepository.findByUsername(username);
    }

    public List<MonthTicket> getAllMonthTicket() {
        return monthTicketRepository.findAll();
    }
    public List<MonthTicket> getMonthTicketByParkingName(String parking_name) {
        return monthTicketRepository.findByParking_name(parking_name);
    }

    public Boolean checkTicket(String id_card, String parking_name, String duration){
        MonthTicket monthTicket = monthTicketRepository.findTicket(id_card, parking_name, duration);
        if(monthTicket!=null) return true;
        return false;
    }
    public MonthTicket getTicket(String id_card){
        return monthTicketRepository.findTicketById_card(id_card);
    }
}
