package ru.prod.prod2025java.entities;

import jakarta.persistence.*;
import lombok.Data;
import ru.prod.prod2025java.UserTarget;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class DbUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String email;
    private String password;
    private String name;
    private String surname;
    private String avatarUrl;

    @Embedded
    private UserTarget other;

    @ElementCollection
    private List<UUID> history;
}
