package com.project.SupportFlow.messaging.consumer;

import com.project.SupportFlow.dto.TicketResponseDTO;
import com.project.SupportFlow.model.Ticket;
import com.project.SupportFlow.service.AiResponseService;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.AnyKeyJavaClass;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@AllArgsConstructor
public class TicketConsumer {
    private ObjectMapper objectMapper;

    private AiResponseService aiResponseService;

    @RabbitListener(queues = "supportflow.ticket.created")
    public void receiveMessage(String message){
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
