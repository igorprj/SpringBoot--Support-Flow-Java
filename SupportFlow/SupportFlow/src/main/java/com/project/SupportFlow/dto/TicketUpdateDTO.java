package com.project.SupportFlow.dto;

import com.project.SupportFlow.enums.TicketCategory;
import com.project.SupportFlow.enums.TicketPriority;
import com.project.SupportFlow.enums.TicketStatus;

public record TicketUpdateDTO(
        String title,
        String description,
        TicketStatus status,
        TicketPriority priority,
        TicketCategory category
) {
}
