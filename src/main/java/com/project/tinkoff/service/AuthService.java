package com.project.tinkoff.service;

import com.project.tinkoff.rest.v1.models.request.LoginCredential;
import com.project.tinkoff.rest.v1.models.request.RegisterCredential;
import com.project.tinkoff.rest.v1.models.response.TokenResponse;
import com.project.tinkoff.rest.v1.models.response.UserDto;

public interface AuthService {
    TokenResponse register(RegisterCredential credential);

    TokenResponse login(LoginCredential credential);

    boolean isValidToken(String token);

    UserDto getUser(String token);
}
