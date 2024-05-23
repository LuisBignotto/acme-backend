package br.com.acmeairlines.tickets.service;

import br.com.acmeairlines.tickets.model.Message;
import br.com.acmeairlines.tickets.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message save(Message message) {
        message.setTimestamp(LocalDateTime.now());
        return messageRepository.save(message);
    }

    public List<Message> findByTicketId(Long ticketId) {
        return messageRepository.findByTicketId(ticketId);
    }
}
