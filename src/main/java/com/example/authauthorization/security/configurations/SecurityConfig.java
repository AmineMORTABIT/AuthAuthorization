package com.example.authauthorization.security.configurations;


import com.example.authauthorization.security.filters.JWTAuthenticationFilter;
import com.example.authauthorization.security.filters.JWTAuthorizationFilter;
import com.example.authauthorization.security.service.UserDetailsServiceImpl;
import com.example.authauthorization.security.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    private final SecurityUtils securityUtils;


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.cors().and()
                   .csrf().disable()
                   .headers().frameOptions().disable()
                   .and()
                   .headers().httpStrictTransportSecurity().disable()
                   .and()
                   .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                   .and()
                   .headers().frameOptions().disable()
                   .and()
                   .authorizeRequests()
                   .antMatchers("/allUsers").authenticated()
                   .antMatchers("/allUsersByAdmin").hasRole("ADMIN")
                   .anyRequest().permitAll().and()
                   .userDetailsService(this.userDetailsServiceImpl)
                   .addFilterBefore(new JWTAuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)), this.securityUtils), UsernamePasswordAuthenticationFilter.class)
                   .addFilterAfter(new JWTAuthorizationFilter(this.securityUtils), UsernamePasswordAuthenticationFilter.class)

                   .build();

    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
