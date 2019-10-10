package com.murray.communications.security.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {


    private final JwtTokenProviderService jwtTokenProviderService;

    public JwtSecurityConfigurer(JwtTokenProviderService jwtTokenProviderService) {
        this.jwtTokenProviderService = jwtTokenProviderService;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        JwtTokenAuthenticationFilter customFilter = new JwtTokenAuthenticationFilter(jwtTokenProviderService);
        http.exceptionHandling()
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .and()
                .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
