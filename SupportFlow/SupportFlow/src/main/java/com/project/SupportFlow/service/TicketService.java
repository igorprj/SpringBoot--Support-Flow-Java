package com.project.SupportFlow.service;

import com.project.SupportFlow.enums.TicketPriority;
import com.project.SupportFlow.enums.TicketStatus;
import com.project.SupportFlow.model.Ticket;
import com.project.SupportFlow.repositories.TicketRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TicketService {

    private TicketRepository ticketRepository;

    public boolean createTicket(Ticket ticket){
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPriority(TicketPriority.MEDIUM);

        ticketRepository.save(ticket);

        return true;
    }

    public List<Ticket> findAllTickets(){
        return ticketRepository.findAll();
    }

    public boolean deleteTicket(Long id){
        if(!ticketRepository.existsById(id)){
            return false;
        }

        ticketRepository.deleteById(id);
        return true;
    }

    public Ticket findTicketById(Long id){
        return ticketRepository.findById(id).get();
    }

    public boolean updateTicket(Long id, Ticket ticket){
        if(!ticketRepository.existsById(id)){
            return false;
        }

        ticketRepository.save(ticket);

        return true;
    }
}
