package com.project.SupportFlow.model;

import com.project.SupportFlow.enums.TicketCategory;
import com.project.SupportFlow.enums.TicketPriority;
import com.project.SupportFlow.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private TicketStatus status;
    private TicketPriority priority;
    private TicketCategory category;
}
