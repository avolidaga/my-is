package ru.se.ifmo.tinder.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.se.ifmo.tinder.model.enums.RequestStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_request")
@Getter
@Setter
@Builder
@NamedQueries({
        @NamedQuery(name = "UserRequest.getAllUserRequestIds", query = "SELECT ur.user_request_id FROM UserRequest ur"),
        @NamedQuery(name = "UserRequest.getDeclinedUserRequestIds", query = "SELECT ur.user_request_id FROM UserRequest ur WHERE ur.status = 'DECLINED'"),
        @NamedQuery(name = "UserRequest.getReadyUserRequest", query = "SELECT ur.user_request_id FROM UserRequest ur WHERE ur.status = 'READY'"),
        @NamedQuery(name = "UserRequest.getInProgressUserRequest", query = "SELECT ur.user_request_id FROM UserRequest ur WHERE ur.status = 'IN_PROGRESS'"),
        @NamedQuery(name = "UserRequest.getNewUserRequest", query = "SELECT ur.user_request_id FROM UserRequest ur WHERE ur.status = 'NEW'")
})
public class UserRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userRequestId;

    @ManyToOne
    @JoinColumn(name = "user_spacesuit_data_id")
    private UserSpacesuitData userSpacesuitData;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
