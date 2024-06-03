package br.com.acmeairlines.tickets.controller;

import br.com.acmeairlines.tickets.client.UserDTO;
import br.com.acmeairlines.tickets.dto.MessageCreateDTO;
import br.com.acmeairlines.tickets.dto.TicketCreateDTO;
import br.com.acmeairlines.tickets.dto.UpdateStatusDTO;
import br.com.acmeairlines.tickets.model.Message;
import br.com.acmeairlines.tickets.model.Ticket;
import br.com.acmeairlines.tickets.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestBody TicketCreateDTO ticketCreateDTO) {
        Ticket ticket = ticketService.createTicket(ticketCreateDTO.getUserId(), ticketCreateDTO.getTitle(), ticketCreateDTO.getDescription());
        return ResponseEntity.ok(ticket);
    }

    @PutMapping("/{ticketId}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable Long ticketId, @RequestBody UpdateStatusDTO updateStatusDTO) {
        Ticket updatedTicket = ticketService.updateTicket(ticketId, updateStatusDTO.getStatus());
        return ResponseEntity.ok(updatedTicket);
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long ticketId) {
        Optional<Ticket> ticket = ticketService.getTicketById(ticketId);
        return ticket.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<Ticket>> getAllTickets(Pageable pageable) {
        return ResponseEntity.ok(ticketService.getAllTickets(pageable));
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long ticketId) {
        ticketService.deleteTicket(ticketId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{ticketId}/messages")
    public ResponseEntity<Message> addMessage(@PathVariable Long ticketId, @RequestBody MessageCreateDTO messageCreateDTO) {
        return ResponseEntity.ok(ticketService.addMessage(ticketId, messageCreateDTO.getSenderId(), messageCreateDTO.getMessage()));
    }

    @GetMapping("/{ticketId}/messages")
    public ResponseEntity<List<Message>> getMessagesByTicketId(@PathVariable Long ticketId) {
        return ResponseEntity.ok(ticketService.getMessagesByTicketId(ticketId));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Ticket>> getUserById(@RequestHeader("Authorization") String token, @PathVariable Long userId) {
        return ResponseEntity.ok(ticketService.getUserById(token, userId));
    }
}

