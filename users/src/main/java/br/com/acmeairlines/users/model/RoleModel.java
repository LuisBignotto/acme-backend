package br.com.acmeairlines.users.model;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Table(name = "roles")
@Entity(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RoleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roleName;
}
