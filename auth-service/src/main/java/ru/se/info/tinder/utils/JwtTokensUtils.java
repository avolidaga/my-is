package ru.se.info.tinder.utils;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.se.info.tinder.dto.ResponseUserDto;
import ru.se.info.tinder.model.UserEntity;

import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class JwtTokensUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.lifetime}")
    private Duration jwtLifetime;

    @SneakyThrows
    public String generateToken(UserEntity user) {
        SignedJWT signedJWT;
        JWTClaimsSet claimsSet;

        List<String> roles = Stream.of(user.getRole())
                .map(r -> r.getRoleName().name())
                .toList();

        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtLifetime.toMillis());

        claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issueTime(issuedDate)
                .expirationTime(expiredDate)
                .claim("roles", roles)
                .build();

        signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        signedJWT.sign(new MACSigner(jwtSecret));

        return signedJWT.serialize();
    }

    public Mono<SignedJWT> check(String token) {
        return Mono.justOrEmpty(createJWS(token))
                .filter(isNotExpired)
                .filter(validSignature);
    }

    private Predicate<SignedJWT> isNotExpired = token ->
            getExpirationDate(token).after(Date.from(Instant.now()));

    private Predicate<SignedJWT> validSignature = token -> {
        try {
            JWSVerifier jwsVerifier = this.buildJWSVerifier();
            return token.verify(jwsVerifier);

        } catch (JOSEException e) {
            e.printStackTrace();
            return false;
        }
    };

    private MACVerifier buildJWSVerifier() {
        try {
            return new MACVerifier(jwtSecret);
        } catch (JOSEException e) {
            e.printStackTrace();
            return null;
        }
    }

    //TODO обработка ошибок

    private SignedJWT createJWS(String token) {
        try {
            return SignedJWT.parse(token);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Date getExpirationDate(SignedJWT token) {
        try {
            return token.getJWTClaimsSet()
                    .getExpirationTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResponseUserDto createUserDto(SignedJWT signedJWTMono) {
        String subject;
        List<String> auths;
        List<SimpleGrantedAuthority> authorities;

        try {
            subject = signedJWTMono.getJWTClaimsSet().getSubject();
            auths = (List<String>) signedJWTMono.getJWTClaimsSet().getClaim("roles");
        } catch (ParseException e) {
            return null;
        }

        return new ResponseUserDto(subject, auths);
    }
}
