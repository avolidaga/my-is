package ru.se.info.tinder.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import ru.se.info.tinder.dto.ImageResponse;
import ru.se.info.tinder.service.ImageService;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ImageController {

    private final ImageService imageService;

    @MessageMapping("/images/download")
    @SendTo("/topic/image/download")
    public ImageResponse getImageById(@Payload String imageId) {
        ImageResponse imageResponse = imageService.getImageById(imageId);
        log.info("Sending image to client");
        return imageResponse;
    }

    @MessageMapping("/images/upload")
    @SendTo("/topic/image/upload")
    public ImageResponse sendImage(@Payload String image) {
        return imageService.saveImage(image);
    }

    @MessageMapping("/images/url")
    @SendTo("/topic/image/url")
    public ImageResponse getImageUrl(@Payload String imageId) {
        return imageService.getImageUrlById(imageId);
    }


    @MessageMapping("/images/delete")
    @SendTo("/topic/image/delete")
    public ImageResponse deleteImageById(@Payload String imageId) {
        return imageService.deleteImageById(imageId);
    }
}
