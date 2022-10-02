package com.example.authauthorization.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.authauthorization.entities.Role;
import com.example.authauthorization.entities.User;
import com.example.authauthorization.security.utils.SecurityUtils;
import com.example.authauthorization.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class JWTController {

    private UserService userService;

    private SecurityUtils securityUtils;

    @GetMapping("/refresh")
    public void refresh(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationToken = request.getHeader("Authorization");
        if(authorizationToken!=null && authorizationToken.startsWith("Bearer ")){
            try{
                Algorithm algorithm = this.securityUtils.algorithm();
                String jwt = authorizationToken.substring(7);
                DecodedJWT decodedJWT = this.securityUtils.decodeJWT(algorithm, jwt);

                String username = decodedJWT.getSubject();
                //check if the user is black listed ???
                User user = userService.findByUsernameOrEmail(username);
                String acessToken = this.securityUtils.generateAccessTokenAfterRefresh(algorithm, user, request);

                Map<String,String> idToken = new HashMap<>();
                idToken.put("access", acessToken);
                idToken.put("refresh", jwt);

                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(),idToken);

            }catch(Exception e){
                response.setHeader("error-message", e.getMessage());
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }

        } else throw  new RuntimeException("Refresh Toke Required !");
    }
}
