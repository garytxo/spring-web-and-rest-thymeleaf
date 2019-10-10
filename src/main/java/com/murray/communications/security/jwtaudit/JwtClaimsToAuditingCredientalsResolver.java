package com.murray.communications.security.jwtaudit;

import com.murray.communications.security.jwt.JwtTokenProviderService;
import io.jsonwebtoken.Claims;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class JwtClaimsToAuditingCredientalsResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProviderService jwtTokenProviderService;

    public JwtClaimsToAuditingCredientalsResolver(JwtTokenProviderService jwtTokenProviderService) {
        this.jwtTokenProviderService = jwtTokenProviderService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Credentials.class)
                && parameter.getParameterType().equals(AuditingCredentials.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest request, WebDataBinderFactory webDataBinderFactory) throws Exception {


        AuditingCredentials credentials = toUserCredentials(request);

        if (Objects.isNull(credentials)) {
            throw new AuthenticationServiceException("Auditing credentials are invalid");
        }

        return credentials;
    }

    /**
     * Convert the Claims Token values into {@link AuditingCredentials}
     *
     * @param request {@link NativeWebRequest} containing the JWY token
     * @return {@link AuditingCredentials}
     */
    AuditingCredentials toUserCredentials(NativeWebRequest request) {

        Claims claims = readClaimsFrom(request);

        if (Objects.isNull(claims)) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "No claims found in request");
        }

        Long userid = jwtTokenProviderService.getLongValueFromClaim(JwtTokenProviderService.USER, claims)
                .orElseThrow(() -> new AuthenticationServiceException("No user id found in header"));

        Long bandWidthId = jwtTokenProviderService.getLongValueFromClaim(JwtTokenProviderService.BANDWIDTH_ID, claims)
                .orElseThrow(() -> new AuthenticationServiceException("No bandwidth id found in header"));


        return new AuditingCredentials(userid, bandWidthId);
    }

    /**
     * Return the claims object that is a request attribute
     */
    Claims readClaimsFrom(NativeWebRequest nativeWebRequest) {
        final String token = jwtTokenProviderService.readJwtTokenFromAuthorizationHeader(nativeWebRequest.getNativeRequest(HttpServletRequest.class));
        return jwtTokenProviderService.getClaimsFrom(token);

    }
}
