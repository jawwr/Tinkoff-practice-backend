package com.project.tinkoff.service;

import com.project.tinkoff.repository.models.User;

public interface TokenService {
    User getUserByToken(String token);

    boolean isTokenValid(String token);

    String generateToken(String userLogin);

    void revokeAllUserTokens(User user);

    void saveUserToken(String jwtToken, User user);
}
