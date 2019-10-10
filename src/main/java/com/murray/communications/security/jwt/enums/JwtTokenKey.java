package com.murray.communications.security.jwt.enums;

public enum JwtTokenKey {

    /**
     * Encrypted user id key in token
     */
    USER_ID("userId"),

    AUTHORIZATION("Authorization"),

    /**
     * Encrypted location id key in token
     */
    PARENT_COMPANY_ID("bandwithId"),

    /**
     * request header prefix
     */
    REQUEST_HEADER_PREFIX("Bearer "),

    /**
     *  claims requests attribute noe
     */
    REQUEST_CLAIMS_ATTRIBUTES_NAME("claims");



    private String name;

    JwtTokenKey(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
