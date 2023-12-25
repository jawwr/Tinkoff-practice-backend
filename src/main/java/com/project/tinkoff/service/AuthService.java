package com.project.tinkoff.service;

import com.project.tinkoff.rest.v1.models.request.LoginCredential;
import com.project.tinkoff.rest.v1.models.request.RegisterCredential;
import com.project.tinkoff.rest.v1.models.response.TokenResponse;

public interface AuthService {
    TokenResponse register(RegisterCredential credential);

    TokenResponse login(LoginCredential credential);
}
