package com.project.SupportFlow.service;

import com.project.SupportFlow.dto.TicketRequestDTO;
import com.project.SupportFlow.dto.TicketResponseDTO;
import com.project.SupportFlow.dto.TicketUpdateDTO;
import com.project.SupportFlow.enums.TicketCategory;
import com.project.SupportFlow.enums.TicketPriority;
import com.project.SupportFlow.enums.TicketStatus;
import com.project.SupportFlow.model.Ticket;
import com.project.SupportFlow.repositories.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestTicketService {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    void createTicket() {
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

    @Test
    void deveCriarTicketComStatusEPrioridadePadrao() {
        TicketRequestDTO dto = new TicketRequestDTO(
                "test",
                "lalalaal",
                TicketStatus.CLOSED,
                TicketPriority.HIGH,
                TicketCategory.ACCOUNT
        );

        Ticket ticketSalva = new Ticket();
        ticketSalva.setTitle(dto.title());
        ticketSalva.setDescription(dto.description());
        ticketSalva.setStatus(TicketStatus.OPEN);
        ticketSalva.setPriority(TicketPriority.MEDIUM);
        ticketSalva.setCategory(dto.category());


        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticketSalva);

        TicketResponseDTO resultado = ticketService.createTicket(dto);

        assertThat(resultado).isNotNull();
        assertThat(resultado.title()).isEqualTo(dto.title());
        assertThat(resultado.description()).isEqualTo(dto.description());
        assertThat(resultado.status()).isEqualTo(TicketStatus.OPEN);
        assertThat(resultado.priority()).isEqualTo(TicketPriority.MEDIUM);
        assertThat(resultado.category()).isEqualTo(dto.category());

    }

    @Test
    void retornaTodosOsTickets() {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitle("test");
        ticket.setDescription("test");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPriority(TicketPriority.MEDIUM);
        ticket.setCategory(TicketCategory.ACCOUNT);

        Ticket ticket2 = new Ticket();
        ticket2.setId(2L);
        ticket2.setTitle("test2");
        ticket2.setDescription("test2");
        ticket2.setStatus(TicketStatus.OPEN);
        ticket2.setPriority(TicketPriority.MEDIUM);
        ticket2.setCategory(TicketCategory.ACCOUNT);

        when(ticketRepository.findAll()).thenReturn(List.of(ticket,ticket2));

        List<TicketResponseDTO> resultado = ticketService.findAllTickets();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).title()).isEqualTo(ticket.getTitle());
        assertThat(resultado.get(0).description()).isEqualTo(ticket.getDescription());
        assertThat(resultado.get(0).status()).isEqualTo(ticket.getStatus());
        assertThat(resultado.get(0).priority()).isEqualTo(ticket.getPriority());
        assertThat(resultado.get(0).category()).isEqualTo(ticket.getCategory());

        assertThat(resultado.get(1).title()).isEqualTo(ticket2.getTitle());
        assertThat(resultado.get(1).description()).isEqualTo(ticket2.getDescription());
        assertThat(resultado.get(1).status()).isEqualTo(ticket2.getStatus());
        assertThat(resultado.get(1).priority()).isEqualTo(ticket2.getPriority());
        assertThat(resultado.get(1).category()).isEqualTo(ticket2.getCategory());
    }

    @Test
    void deveRetornarListaVazia() {
        when(ticketRepository.findAll()).thenReturn(List.of());

        List<TicketResponseDTO> resultado = ticketService.findAllTickets();

        assertThat(resultado).isEmpty();
    }

    @Test
    void deveDeletarTicket() {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitle("test");
        ticket.setDescription("test");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPriority(TicketPriority.MEDIUM);
        ticket.setCategory(TicketCategory.ACCOUNT);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        ticketService.deleteTicket(1L);

        verify(ticketRepository).delete(ticket);

    }

    @Test
    void deveRetornarExceptionNaoEncontrado() {
        Long idInexistente = 999L;
        when(ticketRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            ticketService.deleteTicket(idInexistente);
        });
    }

    @Test
    void deveAcharTicketPorId() {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitle("test");
        ticket.setDescription("test");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPriority(TicketPriority.MEDIUM);
        ticket.setCategory(TicketCategory.ACCOUNT);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        TicketResponseDTO resultado = ticketService.findTicketById(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.title()).isEqualTo(ticket.getTitle());
        assertThat(resultado.description()).isEqualTo(ticket.getDescription());
        assertThat(resultado.status()).isEqualTo(ticket.getStatus());
        assertThat(resultado.priority()).isEqualTo(ticket.getPriority());
        assertThat(resultado.category()).isEqualTo(ticket.getCategory());
    }

    @Test
    void deveLancarExceptionSeNaoEncontrado() {
        Long idInexistente = 999L;

        when(ticketRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            ticketService.deleteTicket(idInexistente);
        });
    }

    @Test
    void deveAtualizarTicket() {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitle("test");
        ticket.setDescription("test test");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPriority(TicketPriority.MEDIUM);
        ticket.setCategory(TicketCategory.ACCOUNT);

        TicketUpdateDTO dto = new TicketUpdateDTO(
                "test",
                "lalalaal",
                TicketStatus.OPEN,
                TicketPriority.MEDIUM,
                TicketCategory.ACCOUNT
        );

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        TicketResponseDTO resultado = ticketService.updateTicket(1L, dto);

        assertThat(resultado).isNotNull();
        assertThat(resultado.title()).isEqualTo(dto.title());
        assertThat(resultado.description()).isEqualTo(dto.description());
        assertThat(resultado.status()).isEqualTo(dto.status());
        assertThat(resultado.priority()).isEqualTo(dto.priority());
        assertThat(resultado.category()).isEqualTo(dto.category());
    }

    @Test
    void deveLancarExceptionIdNaoEncontradoaoAtualizar() {
        TicketUpdateDTO dto = new TicketUpdateDTO(
                "tile",
                "teste",
                TicketStatus.OPEN,
                TicketPriority.MEDIUM,
                TicketCategory.ACCOUNT
        );

        Long idInexistente = 999L;

        when(ticketRepository.findById(idInexistente)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> {
            ticketService.updateTicket(idInexistente, dto);
        });
    }
}
