package com.example.authauthorization.security.filters;

import com.example.authauthorization.security.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private SecurityUtils securityUtils;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationToken = request.getHeader("Authorization");

        if(request.getServletPath().equals("/refresh")){
            filterChain.doFilter(request, response);
        } else {
            if(authorizationToken!=null && authorizationToken.startsWith("Bearer ")){
                try{

                    UsernamePasswordAuthenticationToken token =this.securityUtils.extractUsernameAndRolesFromBearerJwt(authorizationToken);

                    SecurityContextHolder.getContext().setAuthentication(token);

                    filterChain.doFilter(request, response);

                }catch(Exception e){
                    response.setHeader("error-message", e.getMessage());
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                }

            }else {filterChain.doFilter(request, response);}
        }
    }
}
