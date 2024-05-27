package br.com.acmeairlines.tickets.service;

import br.com.acmeairlines.tickets.client.UserClient;
import br.com.acmeairlines.tickets.client.UserDTO;
import br.com.acmeairlines.tickets.model.Message;
import br.com.acmeairlines.tickets.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserClient userClient;

    @Autowired
    private TicketService ticketService;

    public Message save(Message message) {
        message.setTimestamp(LocalDateTime.now());
        return messageRepository.save(message);
    }

    public List<Message> findByTicketId(Long ticketId) {
        return messageRepository.findByTicketId(ticketId);
    }

    public Message createMessage(Message message, String token) {
        Map<String, String> tokenResponse = userClient.checkToken(token);

        if (tokenResponse.containsKey("error")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        String userEmail = tokenResponse.get("email");
        UserDTO userDTO = userClient.getUserByEmail(userEmail);
        message.setSenderId(userDTO.getId());
        return save(message);
    }

    public List<Message> getMessagesByTicketId(Long ticketId, String token) {
        Map<String, String> tokenResponse = userClient.checkToken(token);

        if (tokenResponse.containsKey("error")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        String userEmail = tokenResponse.get("email");
        UserDTO userDTO = userClient.getUserByEmail(userEmail);

        if (ticketService.canAccessTicket(ticketId, userDTO)) {
            return findByTicketId(ticketId);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
    }
}
