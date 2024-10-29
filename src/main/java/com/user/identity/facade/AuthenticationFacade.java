package com.user.identity.facade;

import org.springframework.stereotype.Service;

import com.user.identity.controller.dto.request.AuthenticationRequest;
import com.user.identity.controller.dto.request.IntrospectRequest;
import com.user.identity.controller.dto.request.LogoutRequest;
import com.user.identity.controller.dto.request.RefreshRequest;
import com.user.identity.controller.dto.response.AuthenticationResponse;
import com.user.identity.controller.dto.response.IntrospectResponse;
import com.user.identity.service.AuthenticationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationFacade {
    AuthenticationService authenticationService;

    /**
     * Authenticates a user with the provided credentials.
     *
     * @param request the authentication request containing username and password
     * @return AuthenticationResponse containing the token and authentication status
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }

    /**
     * Logs out the user by invalidating the provided token.
     *
     * @param request the logout request containing the token to invalidate
     */
    public String logout(LogoutRequest request) throws Exception {
        authenticationService.logout(request);
        return "Logout Successfully";
    }

    /**
     * Refreshes the access token using the provided refresh token.
     *
     * @param request the refresh request containing the refresh token
     * @return AuthenticationResponse containing the new token and authentication status
     */
    public AuthenticationResponse refreshToken(RefreshRequest request) throws Exception {
        return authenticationService.refreshToken(request);
    }

    /**
     * Introspects the given token to check its validity.
     *
     * @param introspectRequest the token to introspect
     * @return IntrospectResponse containing validity and associated user information
     */
    public IntrospectResponse introspect(IntrospectRequest introspectRequest) throws Exception {

        return authenticationService.introspect(introspectRequest);
    }
}
