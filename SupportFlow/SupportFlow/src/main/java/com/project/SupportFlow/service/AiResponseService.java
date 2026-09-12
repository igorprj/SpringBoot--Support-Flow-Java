package com.project.SupportFlow.service;

import com.project.SupportFlow.model.AIResponse;
import com.project.SupportFlow.model.Ticket;
import com.project.SupportFlow.repositories.AIResponseRepository;
import com.project.SupportFlow.repositories.TicketRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AiResponseService {

    private AIResponseRepository aiResponseRepository;



    public AIResponse processTicket(Ticket ticket) {
        AIResponse aiResponse = new AIResponse();
        aiResponse.setResponse("Resposta IA");
        aiResponse.setTicket(ticket);

        return aiResponseRepository.save(aiResponse);
    }
}
