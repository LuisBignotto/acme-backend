package br.com.acmeairlines.tickets.controller;

import br.com.acmeairlines.tickets.model.Ticket;
import br.com.acmeairlines.tickets.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.createTicket(ticket));
    }

    @GetMapping
    public ResponseEntity<Page<Ticket>> getAllTickets(Pageable pageable) {;
        return ResponseEntity.ok(ticketService.findAllTickets(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicket(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        Ticket ticket = ticketService.getTicket(id, token);
        return ResponseEntity.ok(ticket);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Ticket>> getTicketsByUserId(@PathVariable Long userId, @RequestHeader("Authorization") String token) {
        List<Ticket> tickets = ticketService.getTicketsByUserId(userId, token);
        return ResponseEntity.ok(tickets);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        ticketService.deleteTicket(id, token);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable Long id, @RequestBody Ticket ticket, @RequestHeader("Authorization") String token) {
        Ticket updatedTicket = ticketService.updateTicket(id, ticket, token);
        return ResponseEntity.ok(updatedTicket);
    }
}
