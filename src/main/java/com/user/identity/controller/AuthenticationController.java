package com.user.identity.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.identity.dto.ApiResponse;
import com.user.identity.dto.request.AuthenticationRequest;
import com.user.identity.dto.request.IntrospectRequest;
import com.user.identity.dto.request.LogoutRequest;
import com.user.identity.dto.request.RefreshRequest;
import com.user.identity.dto.response.AuthenticationResponse;
import com.user.identity.dto.response.IntrospectResponse;
import com.user.identity.facade.AuthenticationFacade;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationController {
    AuthenticationFacade authenticationFacade;

    // dang nhap -> token
    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationFacade.authenticate(request);
        return ApiResponse.success(result);
    }

    // api nay co nhiem vu kiem tra token co hop le hay khong
    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws Exception {

        var result = authenticationFacade.introspect(request);
        return ApiResponse.success(result);
    }

    // lam moi token
    @PostMapping("/refresh")
    public ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshRequest request) throws Exception {

        var result = authenticationFacade.refreshToken(request);
        return ApiResponse.success(result);
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(@RequestBody LogoutRequest request) throws Exception {

        var result = authenticationFacade.logout(request);
        return ApiResponse.success(result);
    }
}
