package com.murray.communications.controllers.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User at
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthenticationRequest {

    private String username;
    private String password;
}
