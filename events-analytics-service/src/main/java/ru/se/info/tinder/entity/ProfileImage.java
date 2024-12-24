package ru.se.info.tinder.entity;

import lombok.Data;
import ru.se.info.tinder.dto.MessageType;

import javax.persistence.*;

@Data
@Entity
@Table(name = "profile_images_log")
public class ProfileImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_id", nullable = false, unique = true)
    private String imageId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false)
    private MessageType type;
}
