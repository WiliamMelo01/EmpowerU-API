package org.wiliammelo.empoweru.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.wiliammelo.empoweru.exceptions.CustomException;
import org.wiliammelo.empoweru.models.User;

import java.time.Instant;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(User user, GrantedAuthority grantedAuthority) throws CustomException {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        try {
            return JWT.create()
                    .withIssuer("EmpowerU-api")
                    .withSubject(user.getEmail())
                    .withExpiresAt(this.getExpirationTime())
                    .withClaim("role", grantedAuthority.getAuthority().replace("ROLE_", ""))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new CustomException("Error while generating token", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("EmpowerU-api")
                    .build();

            return verifier.verify(token).getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    public String getRole(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("EmpowerU-api")
                    .build();

            return verifier.verify(token).getClaim("role").asString();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    private Instant getExpirationTime() {
        short SECONDS_PER_MINUTE = 60;
        int HOUR_IN_SECONDS = SECONDS_PER_MINUTE * 60;
        return Instant.now().plusSeconds(HOUR_IN_SECONDS);
    }

}
