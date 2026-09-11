package com.project.SupportFlow.messaging.consumer;

import com.project.SupportFlow.dto.TicketResponseDTO;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.AnyKeyJavaClass;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@AllArgsConstructor
public class TicketConsumer {
    private ObjectMapper objectMapper;

    @RabbitListener(queues = "supportflow.ticket.created")
    public void receiveMessage(String message){
        TicketResponseDTO dto = objectMapper.readValue(message, TicketResponseDTO.class);

        System.out.println("Ticket recebido: " + dto);
    }
}
