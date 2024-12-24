package ru.se.info.tinder.entity;

import lombok.Data;
import ru.se.info.tinder.dto.MessageType;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users_log")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "username", nullable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false)
    private MessageType type;
}

