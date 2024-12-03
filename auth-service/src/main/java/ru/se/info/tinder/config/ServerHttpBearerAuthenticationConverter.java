package ru.se.info.tinder.config;

import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.se.info.tinder.utils.JwtTokensUtils;

import java.text.ParseException;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ServerHttpBearerAuthenticationConverter implements Function<ServerWebExchange, Mono<Authentication>> {

    private static final String BEARER = "Bearer ";
    private static final Predicate<String> matchBearerLength = authValue -> authValue.length() > BEARER.length();
    private static final Function<String, Mono<String>> isolateBearerValue = authValue -> Mono.justOrEmpty(authValue.substring(BEARER.length()));
    private final JwtTokensUtils jwtVerifier;

    @Override
    public Mono<Authentication> apply(ServerWebExchange serverWebExchange) {
        return Mono.justOrEmpty(serverWebExchange)
                .flatMap(this::extract)
                .filter(matchBearerLength)
                .flatMap(isolateBearerValue)
                .flatMap(jwtVerifier::check)
                .flatMap(this::create).log();
    }

    public Mono<String> extract(ServerWebExchange serverWebExchange) {
        return Mono.justOrEmpty(serverWebExchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION));
    }

    public Mono<Authentication> create(SignedJWT signedJWTMono) {
        SignedJWT signedJWT = signedJWTMono;
        String subject;
        List<String> auths;
        List<SimpleGrantedAuthority> authorities;

        try {
            subject = signedJWT.getJWTClaimsSet().getSubject();
            auths = (List<String>) signedJWT.getJWTClaimsSet().getClaim("roles");
        } catch (ParseException e) {
            return Mono.empty();
        }
        authorities = auths.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(subject, null, authorities));

    }
}