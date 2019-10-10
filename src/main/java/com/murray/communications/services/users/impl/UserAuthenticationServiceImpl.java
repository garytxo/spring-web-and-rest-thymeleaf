package com.murray.communications.services.users.impl;

import com.murray.communications.domain.entities.users.ApplicationUser;
import com.murray.communications.domain.respository.users.ApplicationUserRepository;
import com.murray.communications.security.jwt.JwtTokenProviderService;
import com.murray.communications.services.users.UserAuthenticatonService;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

public class UserAuthenticationServiceImpl implements UserAuthenticatonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthenticationServiceImpl.class);


    private final AuthenticationManager authenticationManager;

    private final ApplicationUserRepository userRepository;

    private final JwtTokenProviderService jwtTokenProviderService;

    public UserAuthenticationServiceImpl(AuthenticationManager authenticationManager, ApplicationUserRepository userRepository,
                                         JwtTokenProviderService jwtTokenProviderService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtTokenProviderService = jwtTokenProviderService;
    }

    /**
     * {@inheritDoc}
     **/
    @Transactional
    @Override
    public String signIn(@NonNull String userName, @NonNull String password) {
        try {
            LOGGER.debug("User {} attempting signin with pwd:{}", userName, password);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
            ApplicationUser userFound =
                    this.userRepository.findByUsernameAndPassword(userName, password)
                            .orElseThrow(() -> new UsernameNotFoundException("Username " + userName + " not found"));

            return jwtTokenProviderService.createToken(userFound);


        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied", e);
        }
    }


}
