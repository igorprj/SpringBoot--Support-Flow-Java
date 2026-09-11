package com.project.SupportFlow.messaging.producer;

import com.project.SupportFlow.dto.TicketResponseDTO;
import com.project.SupportFlow.enums.TicketCategory;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@AllArgsConstructor
public class TickerProducer {

    private RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper;

    public void sendTicket(TicketResponseDTO dto) {
        rabbitTemplate.convertAndSend("supportflow.ticket.exchange","supportflow.created", objectMapper.writeValueAsString(dto));
    }
}
