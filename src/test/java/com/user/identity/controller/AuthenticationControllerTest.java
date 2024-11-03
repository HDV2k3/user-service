package com.user.identity.controller;

import com.user.identity.controller.dto.ApiResponse;
import com.user.identity.controller.dto.request.AuthenticationRequest;
import com.user.identity.controller.dto.response.AuthenticationResponse;
import com.user.identity.facade.AuthenticationFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthenticationFacade authenticationFacade;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    void authenticate_Success() {
        // Given
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("test@example.com");
        request.setPassword("ValidPass123!");

        AuthenticationResponse expectedResponse = new AuthenticationResponse();
        expectedResponse.setAuthenticated(true);
        expectedResponse.setToken("testToken");

        when(authenticationFacade.authenticate(request)).thenReturn(expectedResponse);

        // When
        ApiResponse<AuthenticationResponse> response = authenticationController.authenticate(request);

        // Then
        assertNotNull(response);
        assertTrue(response.getData().isAuthenticated());
        assertEquals("testToken", response.getData().getToken());
        assertEquals(HttpStatus.OK, response.getResponseCode());
        verify(authenticationFacade, times(1)).authenticate(request);
    }
}