package com.project.SupportFlow.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ai_responses")
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class AIResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String response;

    @ManyToOne
    private Ticket ticket;
}
