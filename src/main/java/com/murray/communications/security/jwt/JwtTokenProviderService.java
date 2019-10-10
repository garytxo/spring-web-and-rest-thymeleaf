package com.murray.communications.security.jwt;

import com.murray.communications.domain.entities.users.ApplicationUser;
import com.murray.communications.exceptions.InvalidJwtAuthenticationException;
import com.murray.communications.security.jwt.enums.JwtTokenKey;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class JwtTokenProviderService {

    public static final String USER = "userId";
    public static final String BANDWIDTH_ID = "bandwidthId";
    public static final String ROLES = "roles";
    private String secretKey;

    private long validityInMilliseconds;

    private UserDetailsService userDetailsService;

    public JwtTokenProviderService(final String secretKey, long validityInMilliseconds, UserDetailsService userDetailsService) {
        this.secretKey = secretKey;
        this.validityInMilliseconds = validityInMilliseconds;
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /**
     * Create the JWT token which will contain the userName and the user roles
     *
     * @param applicationUser {@link ApplicationUser}
     * @return encrypted string
     */
    public String createToken(ApplicationUser applicationUser) {

        Claims claims = Jwts.claims().setSubject(applicationUser.getUsername());
        claims.put(ROLES, applicationUser.gerRolesOnly());
        claims.put(USER, applicationUser.getId());
        claims.put(BANDWIDTH_ID, applicationUser.getBandWidthCredentials().getId());

        Instant issuedAt = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant expiration = issuedAt.plus(3, ChronoUnit.MINUTES);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(valueOf(issuedAt))
                .setExpiration(valueOf(expiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * Returns the Long value that is stored in the Claims under the claimKey
     * @param claimKey String key containing the claims value
     * @param claims {@link Claims}
     * @return Long
     */
    public Optional<Long> getLongValueFromClaim(String claimKey,Claims claims){

        if(Objects.isNull(claims)){
            throw new IllegalArgumentException("Claim was null");
        }

        if(!claims.containsKey(claimKey)){
            throw new IllegalArgumentException(String.format("Claim key %s does not exist",claimKey));
        }

        return Optional.ofNullable(claims.get(claimKey,Long.class));


    }



    /**
     * Checks the http request for an authorization header and return the JWT token if exists.
     *
     * @param request
     * @throws InvalidJwtAuthenticationException
     */
    public String readJwtTokenFromAuthorizationHeader(HttpServletRequest request) throws InvalidJwtAuthenticationException {


        final Optional<String> authHeader = Optional.ofNullable(request.getHeader(JwtTokenKey.AUTHORIZATION.getName()));

        if (!authHeader.isPresent() || !authHeader.orElse("").startsWith(JwtTokenKey.REQUEST_HEADER_PREFIX.getName())) {
            throw  new InvalidJwtAuthenticationException("Missing authorisation header");
        }
        log.info("> Header:{}",authHeader.get());

        return authHeader.get().substring((JwtTokenKey.REQUEST_HEADER_PREFIX.getName().length()));
    }


    /**
     * Returns the Jwt {@link Claims} by parsing the decoded token using the secret key.
     *
     * @param token jwt encrypted token
     * @return {@link Claims}
     */
    public Claims getClaimsFrom(final String token)  {

        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }


    /**
     * Return the bandwidth id from the token
     * @param token
     * @return
     */
    public Long getBandwidthIdFrom(final String token) {
        Claims claims = getClaimsFrom(token);
        return claims.get(BANDWIDTH_ID,Long.class);

    }


    /**
     * Convert {@link Instant} value to Date
     * @param instant
     * @return Date
     */
    private Date valueOf(Instant instant) {

        return Date.from(instant);
    }

    Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJwtAuthenticationException("Expired or invalid JWT token");
        }
    }

}
