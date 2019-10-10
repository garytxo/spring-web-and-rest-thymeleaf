package com.murray.communications.security.impl;

import com.murray.communications.domain.respository.users.ApplicationUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final ApplicationUserRepository userRepository;

    public CustomUserDetailsServiceImpl(ApplicationUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {


        return this.userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("Opps,username: " + userName + " not found"));
    }
}
