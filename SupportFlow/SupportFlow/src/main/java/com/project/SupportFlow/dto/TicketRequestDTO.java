package com.project.SupportFlow.dto;

import com.project.SupportFlow.enums.TicketCategory;
import com.project.SupportFlow.enums.TicketPriority;
import com.project.SupportFlow.enums.TicketStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TicketRequestDTO(
        @NotBlank
        String title,
        @NotBlank
        String description,
        @NotNull
        TicketCategory category
) {
}
