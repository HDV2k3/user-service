package com.user.identity.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.identity.controller.dto.ApiResponse;
import com.user.identity.controller.dto.request.AuthenticationRequest;
import com.user.identity.controller.dto.request.IntrospectRequest;
import com.user.identity.controller.dto.request.LogoutRequest;
import com.user.identity.controller.dto.request.RefreshRequest;
import com.user.identity.controller.dto.response.AuthenticationResponse;
import com.user.identity.controller.dto.response.IntrospectResponse;
import com.user.identity.facade.AuthenticationFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Authentication Controller", description = "Endpoints for user authentication and token management")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationController {
    AuthenticationFacade authenticationFacade;

    /**
     * Authenticate user and generate a token.
     *
     * @param request the authentication request containing credentials
     * @return a successful response containing the authentication token
     */
    @PostMapping("/login")
    @Operation(summary = "Login user",
            description = "Authenticate the user and return a JWT token."
          )
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationFacade.authenticate(request);
        return ApiResponse.success(result);
    }

    /**
     * Check the validity of the token.
     *
     * @param request the introspect request containing the token
     * @return a response indicating whether the token is valid or not
     */
    @PostMapping("/introspect")
    @Operation(summary = "Introspect token",
            description = "Check if the provided token is valid."
         )
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws Exception {
        var result = authenticationFacade.introspect(request);
        return ApiResponse.success(result);
    }

    /**
     * Refresh the authentication token.
     *
     * @param request the refresh request containing the old token
     * @return a new authentication response with a new token
     */
    @PostMapping("/refresh")
    @Operation(summary = "Refresh token",
            description = "Generate a new token using the refresh token."
         )
    public ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshRequest request) throws Exception {
        var result = authenticationFacade.refreshToken(request);
        return ApiResponse.success(result);
    }

    /**
     * Logout user and invalidate the token.
     *
     * @param request the logout request containing the token to invalidate
     * @return a response indicating the result of the logout operation
     */
    @PostMapping("/logout")
    @Operation(summary = "Logout user",
            description = "Invalidate the user’s token to log them out."
         )
    public ApiResponse<String> logout(@RequestBody LogoutRequest request) throws Exception {
        var result = authenticationFacade.logout(request);
        return ApiResponse.success(result);
    }
}
