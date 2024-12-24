package ru.se.info.tinder.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.InputStreamResource;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.se.info.tinder.dto.ProfileImageMessage;
import ru.se.info.tinder.dto.ProfileImageResponse;
import ru.se.info.tinder.dto.WebSocketImageResponse;
import ru.se.info.tinder.kafka.SpacesuitRequestProducer;
import ru.se.info.tinder.model.UserData;

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
    public ProfileImageResponse uploadProfileImageByUserDataId(Long userDataId, MultipartFile file) {
        UserData userData = userDataService.getUserDataById(userDataId);
        if (userData.getProfileImageId() != null) {
            deleteProfileImageById(userDataId, userData.getProfileImageId());
        }
        WebSocketImageResponse webSocketImageResponse = sendToImageService(
                "/images/upload",
                Arrays.toString(file.getBytes()),
                "/image/upload"
        ).get();

        if (webSocketImageResponse.isSuccess()) {
            String id = webSocketImageResponse.getPayload();
            userDataService.addProfileImageToUserData(userDataId, id);

            ProfileImageMessage message = new ProfileImageMessage(id, userDataId);
            spacesuitRequestProducer.sendMessageToProfileImageCreatedTopic(message);
            return new ProfileImageResponse(id);
        }
        throw new FindException(webSocketImageResponse.getPayload());
    }

    @SneakyThrows
    public ProfileImageResponse deleteProfileImageById(Long userDataId, String id) {
        UserData userData = userDataService.getUserDataById(userDataId);
        WebSocketImageResponse webSocketImageResponse = sendToImageService(
                "/images/delete",
                id,
                "/image/delete"
        ).get();

        if (webSocketImageResponse.isSuccess()) {
            userDataService.deleteProfileImageToUserData(userDataId);

            ProfileImageMessage message = new ProfileImageMessage(id, userDataId);
            spacesuitRequestProducer.sendMessageToProfileImageDeletedTopic(message);

            return new ProfileImageResponse(id);
        }
        throw new FindException(webSocketImageResponse.getPayload());
    }

    @SneakyThrows
    public InputStreamResource getProfileImageById(Long userDataId, String id) {
        UserData userData = userDataService.getUserDataById(userDataId);
        WebSocketImageResponse webSocketImageResponse = sendToImageService(
                "/images/download",
                id,
                "/image/download"
        ).get();

        if (webSocketImageResponse.isSuccess()) {
            return new InputStreamResource(new ByteArrayInputStream(webSocketImageResponse.getPayload().getBytes()));
        }
        throw new FindException(webSocketImageResponse.getPayload());
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
