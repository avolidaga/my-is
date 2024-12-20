package ru.se.info.tinder.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {
    @Value("${minio.url}")
    private String minioUrl;
    @Value("${minio.accessKey}")
    private String accessKey;
    @Value("${minio.secretKey}")
    private String secretKey;
    @Getter
    @Value("${minio.bucket-name}")
    private String bucketName;

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
