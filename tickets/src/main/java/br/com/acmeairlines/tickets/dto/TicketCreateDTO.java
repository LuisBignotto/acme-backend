package br.com.acmeairlines.tickets.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketCreateDTO {
    private Long userId;
    private String title;
    private String description;
}