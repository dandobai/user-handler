package com.dandobai.user_handler.dtos.authentication;

public class AuthenticationResponse {
    private final String status;
    private final String jwt;

    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
        status = "ok";
    }

    public String getJwt() {
        return jwt;
    }

    public String getStatus() {
        return status;
    }
}