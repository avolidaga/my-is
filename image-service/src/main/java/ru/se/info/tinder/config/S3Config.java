package ru.se.info.tinder.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class S3Config {

    private final String minioUrl = "http://localhost:9000";
    private final String accessKey = "tinder";
    private final String secretKey = "tindertinder";
    @Getter
    private final String bucketName = "profile_images";

    @Bean
    @SneakyThrows
    public MinioClient minioClient() {
        MinioClient minioClient = MinioClient.builder()
                .credentials(accessKey, secretKey)
                .endpoint(minioUrl)
                .build();

        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

        return minioClient;
    }
}
