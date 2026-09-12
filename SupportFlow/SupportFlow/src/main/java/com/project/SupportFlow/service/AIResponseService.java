package com.project.SupportFlow.service;

import com.project.SupportFlow.model.AIResponse;
import com.project.SupportFlow.model.Ticket;
import com.project.SupportFlow.repositories.AIResponseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class AIResponseService {

    private AIResponseRepository aiResponseRepository;

    private AIClient aiclient;

    public AIResponse processTicket(Ticket ticket) throws IOException, InterruptedException{
        String response = aiclient.generateResponse(
                "Analise este ticket: " + ticket.getDescription()
        );

        AIResponse aiResponse = new AIResponse();
        aiResponse.setResponse(response);
        aiResponse.setTicket(ticket);

        return aiResponseRepository.save(aiResponse);
    }
}
