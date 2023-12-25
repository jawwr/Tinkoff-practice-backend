package com.project.tinkoff.service.implementation;

import com.project.tinkoff.repository.TokenRepository;
import com.project.tinkoff.repository.models.Token;
import com.project.tinkoff.repository.models.User;
import com.project.tinkoff.service.JwtService;
import com.project.tinkoff.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;

    @Override
    public String generateToken(String userLogin) {
        return jwtService.generateToken(userLogin);
    }

    @Override
    @Transactional
    public void revokeAllUserTokens(User user) {
        var userTokens = tokenRepository.findAllValidTokenByUserId(user.getId());
        if (userTokens.isEmpty()) {
            return;
        }
        userTokens.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });

        userTokens.forEach(tokenRepository::updateToken);
    }

    @Override
    public void saveUserToken(String jwtToken, User user) {
        Token token = new Token(jwtToken, user);
        tokenRepository.save(token);
    }
}
