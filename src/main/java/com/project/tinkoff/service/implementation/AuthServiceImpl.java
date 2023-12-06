package com.project.tinkoff.service.implementation;

import com.project.tinkoff.repository.UserRepository;
import com.project.tinkoff.repository.models.User;
import com.project.tinkoff.rest.v1.models.request.LoginCredential;
import com.project.tinkoff.rest.v1.models.request.RegisterCredential;
import com.project.tinkoff.rest.v1.models.response.TokenResponse;
import com.project.tinkoff.rest.v1.models.response.UserDto;
import com.project.tinkoff.service.AuthService;
import com.project.tinkoff.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    @Override
    @Transactional
    public TokenResponse register(RegisterCredential credential) {
        if (userRepository.existUserByLogin(credential.login())) {
            throw new IllegalArgumentException("User already exist");
        }
        User user = convertCredentialsToUser(credential);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setRoles(List.of(findRoleByName(UserRole.USER.name())));

        var savedUser = userRepository.save(user);
        var jwt = tokenService.generateToken(user.getLogin());
        tokenService.saveUserToken(jwt, savedUser);

        return new TokenResponse(jwt);
    }

    private User convertCredentialsToUser(RegisterCredential credential) {
        return User
                .builder()
                .username(credential.login())
                .login(credential.login())
                .password(credential.password())
                .build();
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

    private User getWorkerByPin(List<User> users, String pin) {
        User worker = null;
        for (User user : users) {
            if (passwordEncoder.matches(pin, user.getPassword())) {
                worker = user;
            }
        }
        if (worker == null) {
            throw new IllegalArgumentException("Worker with this pin not exist");
        }
        return worker;
    }

    @Override
    public boolean isValidToken(String token) {
        return tokenService.isTokenValid(token);
    }

    @Override
    public UserDto getUser(String token) {
        return UserDto.from(tokenService.getUserByToken(token));
    }
}
