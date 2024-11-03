package com.user.identity.services;

import com.user.identity.controller.dto.request.AuthenticationRequest;
import com.user.identity.controller.dto.response.AuthenticationResponse;
import com.user.identity.event.OnRegistrationCompleteEvent;
import com.user.identity.exception.AppException;
import com.user.identity.exception.ErrorCode;
import com.user.identity.repository.UserRepository;
import com.user.identity.repository.entity.User;
import com.user.identity.service.Impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder(10);
        authenticationService = new AuthenticationServiceImpl(userRepository, null, eventPublisher);
        authenticationService.SIGNER_KEY = "testSignerKey";
        authenticationService.VALID_DURATION = 3600;
        authenticationService.REFRESHABLE_DURATION = 7200;
    }

    @Test
    void authenticate_Success() {
        // Given
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setEnabled(true);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));

        // When
        AuthenticationResponse response = authenticationService.authenticate(request);

        // Then
        assertNotNull(response);
        assertTrue(response.isAuthenticated());
        assertNotNull(response.getToken());
    }

    @Test
    void authenticate_UserNotFound() {
        // Given
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        // When & Then
        AppException exception = assertThrows(AppException.class, () -> {
            authenticationService.authenticate(request);
        });

        assertEquals(ErrorCode.USER_NOT_EXISTED, exception.getErrorCode());
    }

    @Test
    void authenticate_IncorrectPassword() {
        // Given
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("test@example.com");
        request.setPassword("wrongPassword");

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setEnabled(true);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));

        // When & Then
        AppException exception = assertThrows(AppException.class, () -> {
            authenticationService.authenticate(request);
        });

        assertEquals(ErrorCode.UNAUTHENTICATED, exception.getErrorCode());
    }

    @Test
    void authenticate_UserNotEnabled() {
        // Given
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setEnabled(false);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));

        // When
        AuthenticationResponse response = authenticationService.authenticate(request);

        // Then
        assertNotNull(response);
        assertTrue(response.isAuthenticated());
        assertNotNull(response.getToken());
        verify(eventPublisher, times(1)).publishEvent(any(OnRegistrationCompleteEvent.class));
    }
}