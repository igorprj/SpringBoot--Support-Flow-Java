package com.project.SupportFlow.repositories;

import com.project.SupportFlow.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface TicketRepository extends JpaRepository <Ticket,Long> {
}
