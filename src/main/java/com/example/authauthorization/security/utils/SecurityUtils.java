package com.example.authauthorization.security.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.authauthorization.entities.Role;
import com.example.authauthorization.entities.User;
import com.example.authauthorization.security.models.SecurityUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SecurityUtils {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtAccessExpirationInMs}")
    private int jwtAccessExpirationInMs;

    @Value("${app.jwtRefreshExpirationInMs}")
    private int jwtRefreshExpirationInMs;


    public Algorithm algorithm(){
        return Algorithm.HMAC256(jwtSecret);
    }

    public Map<String,String> generateAccesAndResfreshTokens(SecurityUser user, HttpServletRequest request){

        Algorithm algorithm = this.algorithm();

        String acessToken = JWT.create().withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtAccessExpirationInMs))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        String refreshToken = JWT.create().withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtRefreshExpirationInMs))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

        Map<String,String> idToken = new HashMap<>();
        idToken.put("access", acessToken);
        idToken.put("refresh", refreshToken);

        return idToken;
    }


    public UsernamePasswordAuthenticationToken extractUsernameAndRolesFromBearerJwt(String authorizationToken){

        String jwt = authorizationToken.substring(7);
        Algorithm algorithm = this.algorithm();
        DecodedJWT decodedJWT = decodeJWT(algorithm, jwt);
        String username = decodedJWT.getSubject();
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for(String r:roles) {
            authorities.add(new SimpleGrantedAuthority(r));
        }

        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

    public DecodedJWT decodeJWT(Algorithm algorithm, String jwt){


        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
        return decodedJWT;

    }

    public String generateAccessTokenAfterRefresh(Algorithm algorithm, User user, HttpServletRequest request){

        return JWT.create().withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtAccessExpirationInMs))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList()))
                .sign(algorithm);
    }
}
