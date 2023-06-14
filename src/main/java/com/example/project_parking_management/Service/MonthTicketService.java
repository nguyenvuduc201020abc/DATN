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
    public MonthTicket saveMonthTicket(MonthTicket monthTicket){
        return monthTicketRepository.save(monthTicket);
    }
    public List<MonthTicket> getListMonthTicket(String username){
        return monthTicketRepository.findByUsername(username);
    }
}
