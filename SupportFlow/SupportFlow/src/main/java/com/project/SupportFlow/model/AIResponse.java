package com.project.SupportFlow.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ai_responses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AIResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "TEXT")
    private String response;

    @ManyToOne
    private Ticket ticket;
}
