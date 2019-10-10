package com.murray.communications.services.users;

public interface UserAuthenticatonService {

    /**
     * Checks if User exist for the user name and password and if found then
     * generate the JWT token that can be used in further requests.
     *
     * @param userName
     * @param password
     * @return
     */
    String signIn(final String userName, final String password);

}
