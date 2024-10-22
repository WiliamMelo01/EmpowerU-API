package org.wiliammelo.empoweru.configuration.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.wiliammelo.empoweru.exceptions.CustomException;
import org.wiliammelo.empoweru.models.User;

import java.time.Instant;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String secret;

    private static final String DEFAULT_ISSUER = "EmpowerU-api";
    private static final short SECONDS_PER_MINUTE = 60;
    private static final int HOUR_IN_SECONDS = SECONDS_PER_MINUTE * 60;
    private static final int HOURS_IN_DAY = HOUR_IN_SECONDS * 24;

    public String generateAccessToken(User user) throws CustomException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(DEFAULT_ISSUER)
                    .withSubject(user.getEmail())
                    .withExpiresAt(this.generateExpirationTime(HOUR_IN_SECONDS))
                    .withClaim("role", getRoleFromUser(user))
                    .withClaim("type", "ACCESS")
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new CustomException("Error while generating access token", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public String generateRefreshToken(User user) throws CustomException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(DEFAULT_ISSUER)
                    .withSubject(user.getEmail())
                    .withExpiresAt(this.generateExpirationTime(HOURS_IN_DAY * 30))
                    .withClaim("role", getRoleFromUser(user))
                    .withClaim("type", "REFRESH")
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new CustomException("Error while generating refresh token", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public String validateAccessToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(DEFAULT_ISSUER)
                    .withClaim("type", "ACCESS")
                    .build();

            return verifier.verify(token).getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    public String validateRefreshToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(DEFAULT_ISSUER)
                    .withClaim("type", "REFRESH")
                    .build();

            return verifier.verify(token).getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    private Instant generateExpirationTime(int seconds) {
        return Instant.now().plusSeconds(seconds);
    }

    private String getRoleFromUser(User user) {
        return user.getAuthorities().stream().findFirst().get().getAuthority().replace("ROLE_", "");
    }

}
