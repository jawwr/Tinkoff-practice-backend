package com.project.tinkoff.service;

import com.project.tinkoff.repository.models.User;

public interface TokenService {
    String generateToken(String userLogin);

    void revokeAllUserTokens(User user);

    void saveUserToken(String jwtToken, User user);
}
