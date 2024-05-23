package br.com.acmeairlines.tickets.client;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String email;
    private String cpf;
    private String name;
    private String phone;

}