package com.project.SupportFlow.service;

import com.project.SupportFlow.model.AIResponse;
import com.project.SupportFlow.model.Ticket;
import com.project.SupportFlow.repositories.AIResponseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AIResponseService {

    private AIResponseRepository aiResponseRepository;

    private AIClient aiclient;

    public AIResponse processTicket(Ticket ticket) {
        AIResponse aiResponse = new AIResponse();
        aiResponse.setResponse("Resposta IA");
        aiResponse.setTicket(ticket);

        String response = aiclient.generateResponse(
                "Analise este ticket: " + ticket.getDescription()
        );

        return aiResponseRepository.save(aiResponse);
    }
}
