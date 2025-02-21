package org.example.authenticationservice.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtGenerator tokenGenerator;
    private final String frontendUrl = "http://localhost:4200/token-handler";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 authentication success for user: {}", authentication.getName());

        String token = tokenGenerator.generateToken(authentication);
        String redirectUrl = frontendUrl + "?token=" + token;
        log.info("Redirect URL: {}", redirectUrl);
        response.sendRedirect(redirectUrl);
    }
}
