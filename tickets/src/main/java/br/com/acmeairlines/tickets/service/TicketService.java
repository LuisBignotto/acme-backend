package br.com.acmeairlines.tickets.service;

import br.com.acmeairlines.tickets.client.UserClient;
import br.com.acmeairlines.tickets.client.UserDTO;
import br.com.acmeairlines.tickets.model.Ticket;
import br.com.acmeairlines.tickets.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserClient userClient;

    public Ticket save(Ticket ticket) {
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());
        return ticketRepository.save(ticket);
    }

    public Optional<Ticket> findById(Long id) {
        return ticketRepository.findById(id);
    }

    public List<Ticket> findByUserId(Long userId) {
        return ticketRepository.findByUserId(userId);
    }

    public void deleteById(Long id) {
        ticketRepository.deleteById(id);
    }

    public Ticket update(Ticket ticket) {
        ticket.setUpdatedAt(LocalDateTime.now());
        return ticketRepository.save(ticket);
    }

    public boolean canAccessTicket(Long ticketId, UserDTO userDTO) {
        return findById(ticketId)
                .map(ticket -> ticket.getUserId().equals(userDTO.getId()) || userDTO.getRoles().contains("ROLE_ADMIN"))
                .orElse(false);
    }

    public Ticket createTicket(Ticket ticket) {
        return save(ticket);
    }

    public Page<Ticket> findAllTickets(Pageable pageable) {
        return ticketRepository.findAll(pageable);
    }

    public Ticket getTicket(Long id, String token) {
        Map<String, String> tokenResponse = userClient.checkToken(token);

        if (tokenResponse.containsKey("error")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        Optional<Ticket> ticket = findById(id);
        if (ticket.isPresent()) {
            String userEmail = tokenResponse.get("email");
            UserDTO userDTO = userClient.getUserByEmail(userEmail);
            if (ticket.get().getUserId().equals(userDTO.getId()) || userDTO.getRoles().contains("ROLE_ADMIN")) {
                return ticket.get();
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found");
    }

    public List<Ticket> getTicketsByUserId(Long userId, String token) {
        Map<String, String> tokenResponse = userClient.checkToken(token);

        if (tokenResponse.containsKey("error")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        String userEmail = tokenResponse.get("email");
        UserDTO userDTO = userClient.getUserByEmail(userEmail);
        if (userDTO.getId().equals(userId) || userDTO.getRoles().contains("ROLE_ADMIN")) {
            return findByUserId(userId);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
    }

    public void deleteTicket(Long id, String token) {
        Map<String, String> tokenResponse = userClient.checkToken(token);

        if (tokenResponse.containsKey("error")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        String userEmail = tokenResponse.get("email");
        UserDTO userDTO = userClient.getUserByEmail(userEmail);
        Optional<Ticket> ticket = findById(id);
        if (ticket.isPresent() && (ticket.get().getUserId().equals(userDTO.getId()) || userDTO.getRoles().contains("ROLE_ADMIN"))) {
            deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
    }

    public Ticket updateTicket(Long id, Ticket ticket, String token) {
        Map<String, String> tokenResponse = userClient.checkToken(token);

        if (tokenResponse.containsKey("error")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        String userEmail = tokenResponse.get("email");
        UserDTO userDTO = userClient.getUserByEmail(userEmail);
        Optional<Ticket> existingTicket = findById(id);
        if (existingTicket.isPresent() && (existingTicket.get().getUserId().equals(userDTO.getId()) || userDTO.getRoles().contains("ROLE_ADMIN"))) {
            ticket.setId(id);
            return update(ticket);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
    }
}
