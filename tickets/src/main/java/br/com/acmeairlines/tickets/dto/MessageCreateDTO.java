package br.com.acmeairlines.tickets.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageCreateDTO {
    private Long senderId;
    private String message;
}
