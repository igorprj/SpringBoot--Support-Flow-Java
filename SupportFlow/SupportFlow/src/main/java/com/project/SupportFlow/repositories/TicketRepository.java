package com.project.SupportFlow.repositories;

import com.project.SupportFlow.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository <Ticket,Long> {
    List<Ticket> findByUserId(Long id);
}
