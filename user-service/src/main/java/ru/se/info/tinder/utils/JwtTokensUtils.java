package ru.se.info.tinder.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import ru.se.info.tinder.dto.ResponseUserDto;
import ru.se.info.tinder.feign.AuthServiceClient;

import static org.springframework.util.StringUtils.hasText;

@Component
@RequiredArgsConstructor
public class JwtTokensUtils {

    private static final String BEARER = "Bearer ";
    private final AuthServiceClient authServiceClient;

    public ResponseUserDto check(String token) {
        ResponseUserDto userDto = authServiceClient.validateToken(token);
        return userDto;
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (hasText(authHeader) && authHeader.startsWith(BEARER)) {
            return authHeader.substring(BEARER.length());
        }
        return null;
    }
}
