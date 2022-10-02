package com.example.authauthorization.security.filters;

import com.example.authauthorization.security.models.JwtAuthenticationResponse;
import com.example.authauthorization.security.models.LoginRequest;
import com.example.authauthorization.security.models.SecurityUser;
import com.example.authauthorization.security.utils.SecurityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


@AllArgsConstructor
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    private SecurityUtils securityUtils;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequest loginRequestUser = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginRequest.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestUser.getUsername(),
                            loginRequestUser.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException{

        SecurityUser user = (SecurityUser) authResult.getPrincipal();

        Map<String, String> tokens = securityUtils.generateAccesAndResfreshTokens(user, request);

        response.setContentType("application/json");
        JwtAuthenticationResponse jwtAuthResp = new JwtAuthenticationResponse(user,tokens);
        new ObjectMapper().writeValue(response.getOutputStream(),jwtAuthResp);

    }
}
