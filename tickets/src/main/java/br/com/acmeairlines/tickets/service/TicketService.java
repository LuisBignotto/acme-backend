package br.com.acmeairlines.tickets.service;

import br.com.acmeairlines.tickets.model.Message;
import br.com.acmeairlines.tickets.model.Ticket;
import br.com.acmeairlines.tickets.repository.MessageRepository;
import br.com.acmeairlines.tickets.repository.TicketRepository;
import br.com.acmeairlines.tickets.client.UserClient;
import br.com.acmeairlines.tickets.client.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserClient userClient;

    public Ticket createTicket(Long userId, String title, String description) {
        Ticket ticket = new Ticket();
        ticket.setUserId(userId);
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setStatus("OPEN");
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());

        return ticketRepository.save(ticket);
    }

    public Ticket updateTicket(Long ticketId, String status) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);
        if (optionalTicket.isPresent()) {
            Ticket ticket = optionalTicket.get();
            ticket.setStatus(status);
            ticket.setUpdatedAt(LocalDateTime.now());
            return ticketRepository.save(ticket);
        }
        throw new RuntimeException("Ticket not found with id: " + ticketId);
    }

    public Optional<Ticket> getTicketById(Long ticketId) {
        return ticketRepository.findById(ticketId);
    }

    public Page<Ticket> getAllTickets(Pageable pageable) {
        return ticketRepository.findAll(pageable);
    }
    public void deleteTicket(Long ticketId) {
        if (ticketRepository.existsById(ticketId)) {
            ticketRepository.deleteById(ticketId);
        } else {
            throw new RuntimeException("Ticket not found with id: " + ticketId);
        }
    }

    public Message addMessage(Long ticketId, Long senderId, String messageText) {
        Message message = new Message();
        message.setTicketId(ticketId);
        message.setSenderId(senderId);
        message.setMessage(messageText);
        message.setTimestamp(LocalDateTime.now());

        return messageRepository.save(message);
    }

    public List<Message> getMessagesByTicketId(Long ticketId) {
        return messageRepository.findByTicketId(ticketId);
    }

    public List<Ticket> getUserById(String token, Long userId) {
        UserDTO user = userClient.getUserById(token, userId);
        return ticketRepository.findByUserId(user.getId());
    }
}
