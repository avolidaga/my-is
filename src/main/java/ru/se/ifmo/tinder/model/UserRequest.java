package ru.se.ifmo.tinder.model;

import jakarta.persistence.*;
import ru.se.ifmo.tinder.model.enums.RequestStatus;

@Entity
@Table(name = "user_request")
@NamedQueries({
        @NamedQuery(name = "UserRequest.getAllUserRequestIds", query = "SELECT ur.user_request_id FROM UserRequest ur"),
        @NamedQuery(name = "UserRequest.getDeclinedUserRequestIds", query = "SELECT ur.user_request_id FROM UserRequest ur WHERE ur.status = 'DECLINED'"),
        @NamedQuery(name = "UserRequest.getReadyUserRequest", query = "SELECT ur.user_request_id FROM UserRequest ur WHERE ur.status = 'READY'"),
        @NamedQuery(name = "UserRequest.getInProgressUserRequest", query = "SELECT ur.user_request_id FROM UserRequest ur WHERE ur.status = 'IN_PROGRESS'"),
        @NamedQuery(name = "UserRequest.getNewUserRequest", query = "SELECT ur.user_request_id FROM UserRequest ur WHERE ur.status = 'NEW'"),
        @NamedQuery(name = "UserRequest.getListAllByUserRequestIdIn", query = "SELECT ur FROM UserRequest ur WHERE ur.user_request_id IN :idList")
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
}
