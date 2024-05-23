package br.com.acmeairlines.tickets.controller;

import br.com.acmeairlines.tickets.client.UserClient;
import br.com.acmeairlines.tickets.client.UserDTO;
import br.com.acmeairlines.tickets.model.Message;
import br.com.acmeairlines.tickets.model.Ticket;
import br.com.acmeairlines.tickets.service.MessageService;
import br.com.acmeairlines.tickets.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserClient userClient;

    @Autowired
    private TicketService ticketService;

    @PostMapping
    public ResponseEntity<Message> createMessage(@RequestBody Message message, Principal principal) {
        String userEmail = principal.getName();
        UserDTO userDTO = userClient.getUserByEmail(userEmail);
        message.setSenderId(userDTO.getId());
        Message createdMessage = messageService.save(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMessage);
    }

    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<List<Message>> getMessagesByTicketId(@PathVariable Long ticketId, Principal principal) {
        String userEmail = principal.getName();
        UserDTO userDTO = userClient.getUserByEmail(userEmail);

        if (canAccessTicket(ticketId, userDTO)) {
            List<Message> messages = messageService.findByTicketId(ticketId);
            return ResponseEntity.ok(messages);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    private boolean canAccessTicket(Long ticketId, UserDTO userDTO) {
        return ticketService.findById(ticketId)
                .map(ticket -> ticket.getUserId().equals(userDTO.getId()) || userDTO.getRoles().contains("ROLE_ADMIN"))
                .orElse(false);
    }
}
