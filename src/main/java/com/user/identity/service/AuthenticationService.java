package com.user.identity.service;

import java.text.ParseException;

import com.nimbusds.jose.JOSEException;
import com.user.identity.controller.dto.request.AuthenticationRequest;
import com.user.identity.controller.dto.request.IntrospectRequest;
import com.user.identity.controller.dto.request.LogoutRequest;
import com.user.identity.controller.dto.request.RefreshRequest;
import com.user.identity.controller.dto.response.AuthenticationResponse;
import com.user.identity.controller.dto.response.IntrospectResponse;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);

    IntrospectResponse introspect(IntrospectRequest request) throws Exception;

    void logout(LogoutRequest request) throws ParseException, JOSEException;

    AuthenticationResponse refreshToken(RefreshRequest request) throws Exception;
}
