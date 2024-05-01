package br.com.acmeairlines.users.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Table(name = "users")
@Entity(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {

    @Id
    private String id;
    private String name;
    private String email;
    private String cpf;
    private String password;
    private String phone;
    private Boolean active;
    @Embedded
    private Address address;
    @Enumerated(EnumType.STRING)
    private Role role;
}
