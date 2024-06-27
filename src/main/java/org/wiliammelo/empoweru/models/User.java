package org.wiliammelo.empoweru.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


@Data
@Entity(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String email;

    private String password;

    private char gender;

}
