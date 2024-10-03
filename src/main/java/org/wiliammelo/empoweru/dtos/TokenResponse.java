package org.wiliammelo.empoweru.dtos;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

@Getter
public class TokenResponse {

    private final String accessToken;

    private final String refreshToken;

    private final int status;

    private final Timestamp timestamp;

    private final String role;

    public TokenResponse(String accessToken, String refreshToken, String role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.role = role;
        this.status = HttpStatus.OK.value();
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

}
