package ru.se.info.tinder.repository;

import io.minio.MinioClient;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import ru.se.info.tinder.config.S3Config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Log4j2
public class MinioComponent {

    private final S3Config s3Config;
    private final MinioClient minioClient;

    public String getPresignedImageUrl(String imageId) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(s3Config.getBucketName())
                    .object(imageId + "jpg")
                    .expiry(3, TimeUnit.DAYS)
                    .method(Method.GET)
                    .build());
        } catch (Exception ex) {
            log.error("Error occurred during image url getting: ", ex);
            throw ex;
        }
    }


    public String getObject(String imageId) throws Exception {
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket(s3Config.getBucketName())
                .object(imageId + "jpg")
                .build();
        try (InputStream stream = minioClient.getObject(args)) {
            return Base64.getEncoder().encodeToString(stream.readAllBytes());
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
                            .object(imageId + "jpg")
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
                    .object(imageId + "jpg")
                    .stream(imageStream, -1, 10485760)
                    .contentType("image/jpg")
                    .build());
        } catch (Exception ex) {
            log.error("Error occurred during image saving: ", ex);
            throw ex;
        }
    }
}
