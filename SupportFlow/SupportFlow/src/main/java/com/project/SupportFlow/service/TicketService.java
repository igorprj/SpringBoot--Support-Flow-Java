package com.project.SupportFlow.service;

import com.project.SupportFlow.dto.TicketRequestDTO;
import com.project.SupportFlow.dto.TicketResponseDTO;
import com.project.SupportFlow.dto.TicketUpdateDTO;
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

    public TicketResponseDTO createTicket(TicketRequestDTO ticketRequestDTO) {
        Ticket ticket = new Ticket();

        createdEntity(ticket, ticketRequestDTO);

        Ticket saved =  ticketRepository.save(ticket);

        return toDTO(saved);
    }

    public List<TicketResponseDTO> findAllTickets(){
        List<Ticket> tickets = ticketRepository.findAll();

        return tickets.stream()
                .map(this::toDTO)
                .toList();
    }

    public void deleteTicket(Long id){
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        ticketRepository.delete(ticket);
    }

    public TicketResponseDTO findTicketById(Long id){
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        return toDTO(ticket);
    }

    public TicketResponseDTO updateTicket(Long id, TicketUpdateDTO ticketUpdateDTO){
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        updateEntity(ticket, ticketUpdateDTO);

        Ticket saved = ticketRepository.save(ticket);
        return toDTO(saved);
    }

    public void createdEntity(Ticket ticket, TicketRequestDTO ticketRequestDTO){
        ticket.setTitle(ticketRequestDTO.title());
        ticket.setDescription(ticketRequestDTO.description());
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPriority(TicketPriority.MEDIUM);
        ticket.setCategory(ticketRequestDTO.category());
    }

    public TicketResponseDTO toDTO(Ticket ticket){
        TicketResponseDTO dto = new TicketResponseDTO(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getPriority(),
                ticket.getCategory(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt()
        );

        return dto;
    }

    public void updateEntity(Ticket ticket, TicketUpdateDTO ticketUpdateDTO){
        ticket.setTitle(ticketUpdateDTO.title());
        ticket.setDescription(ticketUpdateDTO.description());
        ticket.setStatus(ticketUpdateDTO.status());
        ticket.setPriority(ticketUpdateDTO.priority());
        ticket.setCategory(ticketUpdateDTO.category());
    }
}
