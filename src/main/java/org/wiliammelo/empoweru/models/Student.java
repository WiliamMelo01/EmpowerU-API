package org.wiliammelo.empoweru.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student{

    @Id
    @GeneratedValue(strategy =GenerationType.UUID )
    private UUID id;

    @OneToOne
    private User user;

}
