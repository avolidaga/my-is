package ru.se.info.tinder.entity;

import lombok.Data;
import ru.se.info.tinder.dto.MessageType;
import ru.se.info.tinder.dto.RequestStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "spacesuit_requests_log")
public class SpacesuitRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_request_id", nullable = false)
    private Long userRequestId;

    @Column(name = "spacesuit_data_id", nullable = false)
    private Long spacesuitDataId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false)
    private MessageType type;
}
