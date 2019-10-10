package com.murray.communications.security.jwtaudit;

import com.murray.communications.domain.entities.messages.BandWidthCredentials;
import com.murray.communications.domain.entities.users.ApplicationUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * The message  auditing credentials and bandwidth credentials, which are passed in JWT header token.
 */
@AllArgsConstructor
public class AuditingCredentials {

    /**
     * {@link ApplicationUser} unique identifier
     */
    @NonNull
    @Getter
    private Long userId;

    /**
     * {@link BandWidthCredentials}  unique identifier
     */
    @NonNull
    @Getter
    private Long bandWidthCredentialsId;
}
