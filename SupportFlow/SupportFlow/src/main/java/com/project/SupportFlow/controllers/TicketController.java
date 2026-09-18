package com.project.SupportFlow.controllers;

import com.project.SupportFlow.dto.TicketRequestDTO;
import com.project.SupportFlow.dto.TicketResponseDTO;
import com.project.SupportFlow.dto.TicketUpdateDTO;
import com.project.SupportFlow.exceptions.AIResponseNotFoundException;
import com.project.SupportFlow.exceptions.TicketNotFoundException;
import com.project.SupportFlow.model.AIResponse;
import com.project.SupportFlow.model.Ticket;
import com.project.SupportFlow.repositories.AIResponseRepository;
import com.project.SupportFlow.repositories.TicketRepository;
import com.project.SupportFlow.service.AIResponseService;
import com.project.SupportFlow.service.TicketService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/ticket")
@SecurityRequirement(name = "bearerAuth")
@AllArgsConstructor
public class TicketController {

    private TicketService ticketService;

    private AIResponseRepository aiResponseRepository;

    private TicketRepository ticketRepository;

    @PostMapping
    public ResponseEntity<TicketResponseDTO> createTicket(@Valid @RequestBody TicketRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.createTicket(dto));
    }

    @GetMapping
    public ResponseEntity<List<TicketResponseDTO>> getAllTickets(){
        return ResponseEntity.status(HttpStatus.OK).body(ticketService.findAllTickets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> getTicketById(@Valid @RequestParam Long id){
        return ResponseEntity.status(HttpStatus.OK).body(ticketService.findTicketById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@Valid @PathVariable Long id){
        ticketService.deleteTicket(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> updateTicket(@Valid @PathVariable Long id, @RequestBody TicketUpdateDTO dto){
        return ResponseEntity.status(HttpStatus.OK).body(ticketService.updateTicket(id, dto));
    }

    @GetMapping("/{id}/ai_response")
    public ResponseEntity<AIResponse> analyseTicket(@PathVariable Long id){
        AIResponse response = aiResponseRepository.findByTicketId(id)
                .orElseThrow(() -> new AIResponseNotFoundException("AI Response Not Found!"));

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
}
