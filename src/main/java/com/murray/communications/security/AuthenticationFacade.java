package com.murray.communications.security;

import com.murray.communications.domain.entities.users.ApplicationUser;

/**
 * Custom interface that used to extract the authenticated users specific details.
 */
public interface AuthenticationFacade {

    /**
     * Get the {@link ApplicationUser} via the spring security context
     *
     * @return
     */
    ApplicationUser getAuthenticationUser();

}
