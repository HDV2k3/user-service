package com.user.identity.facade;

import com.user.identity.controller.dto.request.AuthenticationRequest;
import com.user.identity.controller.dto.response.AuthenticationResponse;
import com.user.identity.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationFacadeTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationFacade authenticationFacade;

    @Test
    void authenticate_Success() {
        // Given
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("test@example.com");
        request.setPassword("ValidPass123!");

        AuthenticationResponse expectedResponse = new AuthenticationResponse();
        expectedResponse.setAuthenticated(true);
        expectedResponse.setToken("testToken");

        when(authenticationService.authenticate(request)).thenReturn(expectedResponse);

        // When
        AuthenticationResponse response = authenticationFacade.authenticate(request);

        // Then
        assertNotNull(response);
        assertTrue(response.isAuthenticated());
        assertEquals("testToken", response.getToken());
        verify(authenticationService, times(1)).authenticate(request);
    }
}