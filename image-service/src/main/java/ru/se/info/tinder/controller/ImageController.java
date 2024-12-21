package ru.se.info.tinder.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import ru.se.info.tinder.dto.ImageResponse;
import ru.se.info.tinder.service.ImageService;

@Controller
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @MessageMapping("/images/download")
    @SendTo("/topic/image/download")
    public ImageResponse getImageById(@Payload String imageId) {
        return imageService.getImageById(imageId);
    }

    @MessageMapping("/images/upload")
    @SendTo("/topic/image/upload")
    public ImageResponse sendImage(@Payload String image) {
        return imageService.saveImage(image);
    }

    @MessageMapping("/images/delete")
    @SendTo("/topic/image/delete")
    public ImageResponse deleteImageById(@Payload String imageId) {
        return imageService.deleteImageById(imageId);
    }
}
