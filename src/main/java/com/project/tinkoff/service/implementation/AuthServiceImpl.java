package com.project.tinkoff.service.implementation;

import com.project.tinkoff.mapper.UserMapper;
import com.project.tinkoff.repository.UserRepository;
import com.project.tinkoff.repository.models.User;
import com.project.tinkoff.rest.v1.models.request.LoginCredential;
import com.project.tinkoff.rest.v1.models.request.RegisterCredential;
import com.project.tinkoff.rest.v1.models.response.TokenResponse;
import com.project.tinkoff.service.AuthService;
import com.project.tinkoff.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public TokenResponse register(RegisterCredential credential) {
        if (userRepository.existUserByLogin(credential.login())) {
            throw new IllegalArgumentException("User already exist");
        }
        User user = userMapper.fromRegisterCredential(credential);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        var savedUser = userRepository.save(user);
        var jwt = tokenService.generateToken(user.getLogin());
        tokenService.saveUserToken(jwt, savedUser);

        return new TokenResponse(jwt);
    }

    @Override
    @Transactional
    public TokenResponse login(LoginCredential credential) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credential.login(),
                        credential.password()
                )
        );

        var user = userRepository.findUserByLogin(credential.login());
        var jwt = tokenService.generateToken(user.getLogin());
        tokenService.revokeAllUserTokens(user);
        tokenService.saveUserToken(jwt, user);

        return new TokenResponse(jwt);
    }
}
