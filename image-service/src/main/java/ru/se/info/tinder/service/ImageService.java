package ru.se.info.tinder.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.se.info.tinder.dto.ImageResponse;
import ru.se.info.tinder.repository.MinioComponent;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class ImageService {

    private final MinioComponent minioComponent;

    public ImageResponse getImageById(String imageId) {
        try {
            log.info("Start getting image by id {} from minio", imageId);
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

    public ImageResponse getImageUrlById(String imageId) {
        try {
            log.info("Start getting image URL by id {} from minio", imageId);
            return ImageResponse.builder()
                    .payload(minioComponent.getPresignedImageUrl(imageId))
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
