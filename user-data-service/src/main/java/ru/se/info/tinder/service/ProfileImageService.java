package ru.se.info.tinder.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.se.info.tinder.dto.ProfileImageMessage;
import ru.se.info.tinder.dto.ProfileImageResponse;
import ru.se.info.tinder.dto.WebSocketImageResponse;
import ru.se.info.tinder.kafka.SpacesuitRequestProducer;

import java.io.*;
import java.lang.module.FindException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
@Log4j2
public class ProfileImageService {
    private final UserDataService userDataService;
    private final StompWebSocketClient stompWebSocketClient;
    private final SpacesuitRequestProducer spacesuitRequestProducer;

    @SneakyThrows
    public Mono<ProfileImageResponse> uploadProfileImageByUserDataId(Long userDataId, MultipartFile file) {
        String fileStr = Arrays.toString(file.getBytes());
        return userDataService.getUserDataById(userDataId)
                .flatMap(
                        (userData) -> Mono.fromFuture(() -> sendToImageService(
                                        "/images/upload",
                                        fileStr,
                                        "/image/upload"
                                )).flatMap(
                                        (webSocketImageResponse) -> {
                                            if (webSocketImageResponse.isSuccess()) {
                                                String id = webSocketImageResponse.getPayload();
                                                userDataService.addProfileImageToUserData(userDataId, id);
                                                return Mono.just(new ProfileImageResponse(id));
                                            }
                                            return Mono.error(new FindException(webSocketImageResponse.getPayload()));
                                        }
                                ).subscribeOn(Schedulers.boundedElastic())
                                .doOnSuccess(
                                        (ProfileImageResponse) -> {
                                            ProfileImageMessage message = new ProfileImageMessage(ProfileImageResponse.getImageId(), userDataId);
                                            Mono.fromRunnable(
                                                            () -> spacesuitRequestProducer.sendMessageToProfileImageCreatedTopic(message)
                                                    ).subscribeOn(Schedulers.boundedElastic())
                                                    .onErrorContinue(
                                                            (error, n) -> log.error("Failed to send message to Kafka: ${error.message}")
                                                    ).subscribe();
                                        }
                                )
                );
    }

    public Mono<ProfileImageResponse> deleteProfileImageById(Long userDataId, String id) {
        return userDataService.getUserDataById(userDataId)
                .flatMap(
                        (userData) -> Mono.fromFuture(() -> sendToImageService(
                                        "/images/delete",
                                        id,
                                        "/image/delete"
                                )).flatMap(
                                        (webSocketImageResponse) -> {
                                            if (webSocketImageResponse.isSuccess()) {
                                                userDataService.deleteProfileImageToUserData(userDataId);
                                                return Mono.just(new ProfileImageResponse(id));
                                            }
                                            return Mono.error(new FindException(webSocketImageResponse.getPayload()));
                                        }
                                ).subscribeOn(Schedulers.boundedElastic())
                                .doOnSuccess(
                                        (ProfileImageResponse) -> {
                                            ProfileImageMessage message = new ProfileImageMessage(ProfileImageResponse.getImageId(), userDataId);
                                            Mono.fromRunnable(
                                                            () -> spacesuitRequestProducer.sendMessageToProfileImageDeletedTopic(message)
                                                    ).subscribeOn(Schedulers.boundedElastic())
                                                    .onErrorContinue(
                                                            (error, n) -> log.error("Failed to send message to Kafka: ${error.message}")
                                                    ).subscribe();
                                        }
                                )
                );
    }

    public Mono<OutputStream> getProfileImageById(Long userDataId, String id) {
        return userDataService.getUserDataById(userDataId)
                .flatMap(
                        (userData) -> Mono.fromFuture(() -> sendToImageService(
                                "/images/download",
                                id,
                                "/image/download"
                        )).flatMap(
                                (webSocketImageResponse) -> {
                                    if (webSocketImageResponse.isSuccess()) {
                                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(byteArrayOutputStream));
                                        printWriter.print(webSocketImageResponse.getPayload());
                                        printWriter.flush();
                                        return Mono.just(byteArrayOutputStream);
                                    }
                                    return Mono.error(new FindException(webSocketImageResponse.getPayload()));
                                }
                        )
                );
    }

    @SneakyThrows
    private CompletableFuture<WebSocketImageResponse> sendToImageService(String dest, String payload, String subDest) {
        CompletableFuture<WebSocketImageResponse> future = new CompletableFuture<>();
        StompFrameHandler handler = prepareStompFrameHandler(future);
        stompWebSocketClient.sendMessage(dest, payload, subDest, handler);
        return future;
    }

    private StompFrameHandler prepareStompFrameHandler(CompletableFuture<WebSocketImageResponse> future) {
        return new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return WebSocketImageResponse.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                try {
                    future.complete((WebSocketImageResponse) payload);
                } catch (Exception ex) {
                    future.completeExceptionally(ex);
                }
            }
        };
    }
}
