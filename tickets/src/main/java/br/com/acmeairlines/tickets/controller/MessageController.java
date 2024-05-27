package br.com.acmeairlines.tickets.controller;

import br.com.acmeairlines.tickets.model.Message;
import br.com.acmeairlines.tickets.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<Message> createMessage(@RequestBody Message message, @RequestHeader("Authorization") String token) {
        Message createdMessage = messageService.createMessage(message, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMessage);
    }

    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<List<Message>> getMessagesByTicketId(@PathVariable Long ticketId, @RequestHeader("Authorization") String token) {
        List<Message> messages = messageService.getMessagesByTicketId(ticketId, token);
        return ResponseEntity.ok(messages);
    }
}
