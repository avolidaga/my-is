package ru.se.info.tinder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.se.info.tinder.dto.ImageResponse;
import ru.se.info.tinder.repository.MinioComponent;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final MinioComponent minioComponent;

    public ImageResponse getImageById(String imageId) {
        try {
            return ImageResponse.builder()
                    .payload(minioComponent.getObject(imageId))
                    .isSuccess(true)
                    .build();
        } catch (Exception e) {
            return ImageResponse.builder()
                    .payload(e.getMessage())
                    .isSuccess(false)
                    .build();
        }
    }

    public ImageResponse saveImage(String image) {
        try {
            String imageId = UUID.randomUUID().toString();
            minioComponent.saveImage(image, imageId);
            return ImageResponse.builder()
                    .payload(imageId)
                    .isSuccess(true)
                    .build();
        } catch (Exception e) {
            return ImageResponse.builder()
                    .payload(e.getMessage())
                    .isSuccess(false)
                    .build();
        }
    }

    public ImageResponse deleteImageById(String imageId) {
        try {
            minioComponent.deleteImage(imageId);
            return ImageResponse.builder()
                    .payload(imageId)
                    .isSuccess(true)
                    .build();
        } catch (Exception e) {
            return ImageResponse.builder()
                    .payload(e.getMessage())
                    .isSuccess(false)
                    .build();
        }
    }
}
