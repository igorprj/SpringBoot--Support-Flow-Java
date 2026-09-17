package com.project.SupportFlow.repositories;

import com.project.SupportFlow.model.AIResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AIResponseRepository extends JpaRepository<AIResponse, Integer> {
    Optional<AIResponse> findByTicketId(Long ticketId);
}
