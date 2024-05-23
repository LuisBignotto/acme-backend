package br.com.acmeairlines.tickets.controller;

import br.com.acmeairlines.tickets.client.UserClient;
import br.com.acmeairlines.tickets.client.UserDTO;
import br.com.acmeairlines.tickets.model.Ticket;
import br.com.acmeairlines.tickets.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserClient userClient;

    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket, Principal principal) {
        String userEmail = principal.getName();
        UserDTO userDTO = userClient.getUserByEmail(userEmail);
        ticket.setUserId(userDTO.getId());
        Ticket createdTicket = ticketService.save(ticket);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicket(@PathVariable Long id, Principal principal) {
        Optional<Ticket> ticket = ticketService.findById(id);
        if (ticket.isPresent()) {
            String userEmail = principal.getName();
            UserDTO userDTO = userClient.getUserByEmail(userEmail);
            if (ticket.get().getUserId().equals(userDTO.getId()) || userDTO.getRoles().contains("ROLE_ADMIN")) {
                return ResponseEntity.ok(ticket.get());
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Ticket>> getTicketsByUserId(@PathVariable Long userId) {
        List<Ticket> tickets = ticketService.findByUserId(userId);
        return ResponseEntity.ok(tickets);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable Long id, @RequestBody Ticket ticket) {
        Optional<Ticket> existingTicket = ticketService.findById(id);
        if (existingTicket.isPresent()) {
            ticket.setId(id);
            Ticket updatedTicket = ticketService.update(ticket);
            return ResponseEntity.ok(updatedTicket);
        }
        return ResponseEntity.notFound().build();
    }
}
