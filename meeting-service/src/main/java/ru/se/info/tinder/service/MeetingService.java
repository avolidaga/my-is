package ru.se.info.tinder.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.se.info.tinder.dto.*;
import ru.se.info.tinder.feign.UserServiceClient;
import ru.se.info.tinder.kafka.NotificationProducer;
import ru.se.info.tinder.mapper.FabricTextureMapper;
import ru.se.info.tinder.mapper.MeetingMapper;
import ru.se.info.tinder.mapper.MeetingParticipantMapper;
import ru.se.info.tinder.model.Meeting;
import ru.se.info.tinder.repository.MeetingRepository;
import ru.se.info.tinder.service.exception.NoEntityWithSuchIdException;

import java.security.Principal;

@RequiredArgsConstructor
@Service
@Log4j2
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final UserRequestService userRequestService;
    private final UserServiceClient fabricTextureService;
    private final NotificationProducer notificationProducer;

    public Mono<UserRequestDto> createMeeting(CreateMeetingDto createMeetingDto, String token) {

        return fabricTextureService.getFabricTextureById(createMeetingDto.getFabricTextureId(), token)
                .switchIfEmpty(Mono.error(new NoEntityWithSuchIdException("Fabric texture", createMeetingDto.getFabricTextureId())))
                .map(FabricTextureMapper::toEntityFabricTexture)
                .flatMap(
                        (fabricTexture) -> {
                            Meeting meeting = MeetingMapper
                                    .toEntityMeeting(createMeetingDto, fabricTexture);
                            return Mono.fromCallable(
                                    () -> meetingRepository.save(meeting)
                            ).flatMap(
                                    (savedMeeting) -> userRequestService.createUserRequest(savedMeeting)
                                            .map(MeetingParticipantMapper::toUserRequestDto)
                                            .subscribeOn(Schedulers.boundedElastic())
                                            .doOnSuccess(
                                                    (userRequestDto) -> {
                                                        MeetingMessage message = MeetingParticipantMapper.toSpacesuitRequestMsg(userRequestDto);
                                                        Mono.fromRunnable(
                                                                        () -> notificationProducer.sendMessageToSpacesuitRequestCreatedTopic(message)
                                                                ).subscribeOn(Schedulers.boundedElastic())
                                                                .onErrorContinue(
                                                                        (error, n) -> log.error("Failed to send message to Kafka: ${error.message}")
                                                                ).subscribe();
                                                    }
                                            )
                            );
                        }
                );
    }

    public Flux<MeetingDto> getCurrentUserMeeting(Principal principal) {
        return Flux.fromStream(
                () -> meetingRepository.findAllMeetingByOwnerUserUsername(principal.getName()).stream()
        ).map(MeetingMapper::toMeetingDto);
    }

    public Mono<Object> deleteMeeting(Long MeetingId, Principal principal) {
        return Mono.fromCallable(
                () -> meetingRepository.findById(MeetingId)
                        .orElseThrow(() -> new NoEntityWithSuchIdException("Spacesuit data", MeetingId))
        ).flatMap(
                (Meeting) -> {
                    if (!Meeting.getOwnerUser().getUsername().equals(principal.getName())) {
                        return Mono.error(new IllegalArgumentException("User don't have enough rights for data deleting"));
                    }
                    return Mono.fromRunnable(() -> meetingRepository.delete(Meeting));
                }
        );
    }

    public Mono<MeetingDto> updateMeeting(UpdateMeetingDto MeetingDto, Principal principal, String token) {
        return Mono.fromCallable(
                () -> meetingRepository.findById(MeetingDto.getId())
                        .orElseThrow(() -> new NoEntityWithSuchIdException("User spacesuit data", MeetingDto.getId()))
        ).flatMap(
                (oldMeeting) -> {
                    if (!oldMeeting.getOwnerUser().getUsername().equals(principal.getName())) {
                        throw new IllegalArgumentException("User don't have enough rights for data updating");
                    }
                    Long fabricTextureId = MeetingDto.getFabricTextureId();

                    return fabricTextureService.getFabricTextureById(fabricTextureId, token)
                            .switchIfEmpty(Mono.error(new NoEntityWithSuchIdException("Fabric texture", fabricTextureId)))
                            .map(FabricTextureMapper::toEntityFabricTexture)
                            .flatMap(
                                    (fabricTexture) -> {
                                        Meeting newMeeting = MeetingMapper.toEntityMeeting(MeetingDto, oldMeeting, fabricTexture);
                                        return Mono.fromCallable(
                                                () -> meetingRepository.save(newMeeting)
                                        ).map(MeetingMapper::toMeetingDto);
                                    }
                            );
                }
        );
    }

    public Mono<MeetingDto> getMeeting(Long MeetingId, Principal principal) {

        return Mono.fromCallable(
                () -> meetingRepository.findById(MeetingId)
                        .orElseThrow(() -> new NoEntityWithSuchIdException("User spacesuit data", MeetingId))
        ).flatMap(
                (Meeting) -> {
                    if (!Meeting.getOwnerUser().getUsername().equals(principal.getName())) {
                        return Mono.error(new IllegalArgumentException("User don't have enough rights for data getting"));
                    }
                    return Mono.just(Meeting).map(MeetingMapper::toMeetingDto);
                }
        );
    }
}
