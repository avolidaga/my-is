package ru.se.info.tinder.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("auth-service")
public interface AuthServiceClient {
    @PostMapping("/api/v1/user-management/validation")
    ResponseEntity<Boolean> validateToken(@RequestBody String token);
}
