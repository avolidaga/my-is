package ru.se.info.tinder.repository;

import io.minio.MinioClient;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import ru.se.info.tinder.config.S3Config;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
@Log4j2
public class MinioComponent {

    private final S3Config s3Config;
    private final MinioClient minioClient;

    public String getObject(String imageId) throws Exception {
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket(s3Config.getBucketName())
                .object(imageId)
                .build();
        try (InputStream stream = minioClient.getObject(args)) {
            return new String(stream.readAllBytes());
        } catch (Exception ex) {
            log.error("Error occurred during image getting: ", ex);
            throw ex;
        }
    }

    public void deleteImage(String imageId) throws Exception {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(s3Config.getBucketName())
                            .object(imageId)
                            .build()
            );
        } catch (Exception ex) {
            log.error("Error occurred during image deleting: ", ex);
            throw ex;
        }
    }

    public void saveImage(String image, String imageId) throws Exception {
        try (InputStream imageStream = new ByteArrayInputStream(image.getBytes())) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(s3Config.getBucketName())
                    .object(imageId)
                    .stream(imageStream, -1, 10485760)
                    .build());
        } catch (Exception ex) {
            log.error("Error occurred during image saving: ", ex);
            throw ex;
        }
    }
}
