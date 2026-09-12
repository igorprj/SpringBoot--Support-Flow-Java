package com.project.SupportFlow.messaging.consumer;

import com.project.SupportFlow.dto.TicketResponseDTO;
import com.project.SupportFlow.model.Ticket;
import com.project.SupportFlow.service.AIResponseService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@AllArgsConstructor
public class TicketConsumer {
    private ObjectMapper objectMapper;

    private AIResponseService aiResponseService;

    @RabbitListener(queues = "supportflow.ticket.created")
    public void receiveMessage(String message) throws IOException, InterruptedException {
        TicketResponseDTO dto = objectMapper.readValue(message, TicketResponseDTO.class);

        Ticket ticket = new Ticket();
        ticket.setId(dto.id());
        ticket.setTitle(dto.title());
        ticket.setDescription(dto.description());
        ticket.setStatus(dto.status());
        ticket.setPriority(dto.priority());
        ticket.setCategory(dto.category());
        ticket.setCreatedAt(dto.createdAt());
        ticket.setUpdatedAt(dto.updatedAt());

        aiResponseService.processTicket(ticket);

        System.out.println("Ticket recebido: " + dto);
    }
}
