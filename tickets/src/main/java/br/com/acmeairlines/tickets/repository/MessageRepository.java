package br.com.acmeairlines.tickets.repository;

import br.com.acmeairlines.tickets.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByTicketId(Long ticketId);
}
