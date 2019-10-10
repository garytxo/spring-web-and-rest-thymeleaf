package com.murray.communications.config;

import com.murray.communications.domain.respository.users.ApplicationUserRepository;
import com.murray.communications.security.AuthenticationFacade;
import com.murray.communications.security.impl.AuthenticationFacadeImpl;
import com.murray.communications.security.impl.CustomUserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityWebConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public UserDetailsService userDetailsService(ApplicationUserRepository userRepository) {

        return new CustomUserDetailsServiceImpl(userRepository);
    }

    @Bean
    public AuthenticationFacade authenticationFacade() {

        return new AuthenticationFacadeImpl();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //@formatter:off
         http
                .httpBasic().disable()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/login*").permitAll()
                .antMatchers( "/swagger-ui.html", "/webjars/**", "/swagger-resources/**").permitAll()
                //Handled by the SecurityRestConfig so permit from here
                 .antMatchers("/auth/signin").permitAll()
                 .antMatchers("/v1/sms/**").permitAll()
                 //AUTHENTICATED weburls
                .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .successForwardUrl("/")
                .and()
                .logout()
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login?logout")
                    .permitAll();
                //.and()
               // .exceptionHandling()
                //    .accessDeniedHandler(accessDeniedHandler);
        //@formatter:on
    }


    @Override
    public void configure(WebSecurity web) throws Exception {

        web.ignoring()
                .antMatchers("swagger-ui.html", "/webjars/**",
                        "/resources/**",
                        "/static/**",
                        "/css/**",
                        "/js/**",
                        "/img/**",
                        "/h2-console/**");
    }


}
