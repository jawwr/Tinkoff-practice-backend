package com.project.tinkoff.auth;

import com.project.tinkoff.repository.TokenRepository;
import com.project.tinkoff.repository.models.Token;
import com.project.tinkoff.repository.models.UserDetailImpl;
import com.project.tinkoff.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            filterChain.doFilter(request, response);
            return;
        }
        String jwt = authHeader.substring(7);
        Token userToken = tokenRepository.findByToken(jwt);
        if (userToken == null) {
            filterChain.doFilter(request, response);
            return;
        }
        String userLogin = jwtService.extractLogin(jwt);
        UserDetailImpl userDetails = (UserDetailImpl) userDetailsService.loadUserByUsername(userLogin);
        boolean isTokenValid = !userToken.isRevoked() && !userToken.isExpired() && userToken.getUser().getId() == userDetails.getId();
        if (jwtService.isTokenValid(jwt, userDetails.getLogin()) && isTokenValid) {
            var token = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        filterChain.doFilter(request, response);
    }
}
