package com.project.SupportFlow.controllers;

import com.project.SupportFlow.dto.TicketRequestDTO;
import com.project.SupportFlow.dto.TicketResponseDTO;
import com.project.SupportFlow.dto.TicketUpdateDTO;
import com.project.SupportFlow.model.Ticket;
import com.project.SupportFlow.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/ticket")
@AllArgsConstructor
public class TicketController {

    private TicketService ticketService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TicketResponseDTO> createTicket(@RequestBody TicketRequestDTO dto){
        return ResponseEntity.ok(ticketService.createTicket(dto));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TicketResponseDTO>> getAllTickets(){
        return ResponseEntity.ok(ticketService.findAllTickets());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TicketResponseDTO> getTicketById(@RequestParam Long id){
        return ResponseEntity.ok(ticketService.findTicketById(id));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTicket(@PathVariable Long id){
        ticketService.deleteTicket(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TicketResponseDTO> updateTicket(@RequestBody @PathVariable long id, TicketUpdateDTO dto){
        return ResponseEntity.ok(ticketService.updateTicket(id, dto));
    }
}
