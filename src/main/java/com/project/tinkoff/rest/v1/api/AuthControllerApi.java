package com.project.tinkoff.rest.v1.api;

import com.project.tinkoff.rest.v1.models.request.LoginCredential;
import com.project.tinkoff.rest.v1.models.request.RegisterCredential;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/auth")
@Tag(name = "Аутентификация")
public interface AuthControllerApi {
    @Operation(summary = "Регистрация пользователей")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    ref = "jwtResponse"
            )
    })
    @PostMapping("/register")
    ResponseEntity<?> register(@RequestBody RegisterCredential credential);

    @Operation(summary = "Логин пользователя")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    ref = "jwtResponse"
            )
    })
    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody LoginCredential credential);

    @GetMapping("/isValid")
    ResponseEntity<?> isValidToken(@RequestParam("token") String token);

    @GetMapping("/getUserId")
    ResponseEntity<?> getUserId(@RequestParam("token") String token);
}
