package ru.se.info.tinder.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.se.info.tinder.feign.AuthServiceClient;
import ru.se.info.tinder.utils.JwtTokensUtils;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilterConfig extends OncePerRequestFilter {
    private final AuthServiceClient authServiceClient;
    private final JwtTokensUtils jwtTokensUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokensUtils.getTokenFromRequest(request);
        if (token != null && validateToken(token)) {
            String username = jwtTokensUtils.getUsernameFromToken(token);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username,
                        null,
                        jwtTokensUtils.getRolesFromToken(token).stream().map(SimpleGrantedAuthority::new).toList());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean validateToken(String token) {
        return authServiceClient.validateToken(token).getBody().booleanValue();
    }
}