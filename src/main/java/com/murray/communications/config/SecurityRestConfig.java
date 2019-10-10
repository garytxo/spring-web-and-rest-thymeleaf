package com.murray.communications.config;

import com.murray.communications.security.jwt.JwtSecurityConfigurer;
import com.murray.communications.security.jwt.JwtTokenProviderService;
import com.murray.communications.security.jwtaudit.JwtClaimsToAuditingCredientalsResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@Order(2)
public class SecurityRestConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    @Autowired
    private JwtTokenProviderService jwtTokenProviderService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //@formatter:off
        http
            .httpBasic().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .authorizeRequests()
                .antMatchers("/auth/signin").permitAll()
                .antMatchers("/v1/sms/**").authenticated()
                .anyRequest().authenticated()
                .and()
                .apply(new JwtSecurityConfigurer(jwtTokenProviderService));
        //@formatter:on
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {

        resolvers.add(jwtRequestHeaderMethodArgumentResolver());
    }

    @Bean
    public HandlerMethodArgumentResolver jwtRequestHeaderMethodArgumentResolver() {

        return new JwtClaimsToAuditingCredientalsResolver(jwtTokenProviderService);
    }


}
