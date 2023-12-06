package com.project.tinkoff.rest.v1.controller;

import com.project.tinkoff.rest.v1.api.AuthControllerApi;
import com.project.tinkoff.rest.v1.models.request.LoginCredential;
import com.project.tinkoff.rest.v1.models.request.RegisterCredential;
import com.project.tinkoff.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthControllerApi {
    private final AuthService service;

    @Override
    public ResponseEntity<?> register(RegisterCredential credential) {
        return ResponseEntity.ok(service.register(credential));
    }

    @Override
    public ResponseEntity<?> login(LoginCredential credential) {
        return ResponseEntity.ok(service.login(credential));
    }
}
