package com.project.SupportFlow;

import com.project.SupportFlow.dto.TicketRequestDTO;
import com.project.SupportFlow.dto.TicketResponseDTO;
import com.project.SupportFlow.enums.TicketCategory;
import com.project.SupportFlow.enums.TicketPriority;
import com.project.SupportFlow.enums.TicketStatus;
import com.project.SupportFlow.model.Ticket;
import com.project.SupportFlow.repositories.TicketRepository;
import com.project.SupportFlow.service.TicketService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestTicketService {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    public void createTicket() {
        TicketRequestDTO dto = new TicketRequestDTO(
                "test",
                "lalalaal",
                TicketStatus.OPEN,
                TicketPriority.MEDIUM,
                TicketCategory.ACCOUNT
        );

        Ticket ticketSalva = new Ticket();
        ticketSalva.setTitle(dto.title());
        ticketSalva.setDescription(dto.description());
        ticketSalva.setStatus(dto.status());
        ticketSalva.setPriority(dto.priority());
        ticketSalva.setCategory(dto.category());


        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticketSalva);

        TicketResponseDTO resultado = ticketService.createTicket(dto);

        assertThat(resultado).isNotNull();
        assertThat(resultado.title()).isEqualTo(dto.title());
        assertThat(resultado.description()).isEqualTo(dto.description());
        assertThat(resultado.status()).isEqualTo(dto.status());
        assertThat(resultado.priority()).isEqualTo(dto.priority());
        assertThat(resultado.category()).isEqualTo(dto.category());
    }
}
