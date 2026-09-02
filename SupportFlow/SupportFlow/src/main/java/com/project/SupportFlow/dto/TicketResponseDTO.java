package com.project.SupportFlow.dto;

import com.project.SupportFlow.enums.TicketCategory;
import com.project.SupportFlow.enums.TicketPriority;
import com.project.SupportFlow.enums.TicketStatus;

import java.time.LocalDateTime;

public record TicketResponseDTO(
        Long id,
        String title,
        String description,
        TicketStatus status,
        TicketPriority priority,
        TicketCategory category,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
