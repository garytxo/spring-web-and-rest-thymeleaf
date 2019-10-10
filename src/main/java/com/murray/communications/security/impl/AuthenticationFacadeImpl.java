package com.murray.communications.security.impl;

import com.murray.communications.domain.entities.users.ApplicationUser;
import com.murray.communications.security.AuthenticationFacade;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * {@inheritDoc}
 */
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationUser getAuthenticationUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return getUserFrom(authentication).orElseThrow(() -> new IllegalArgumentException("No user found"));
    }

    private Optional<ApplicationUser> getUserFrom(Authentication authentication) {

        return Optional.ofNullable(((ApplicationUser) authentication.getPrincipal()));

    }
}
