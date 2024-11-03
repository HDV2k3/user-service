package com.user.identity.facade;
import com.user.identity.controller.dto.request.UserCreationRequest;
import com.user.identity.controller.dto.response.UserResponse;
import com.user.identity.exception.AppException;
import com.user.identity.exception.ErrorCode;
import com.user.identity.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceFacadeTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserFacade userServiceFacade;

    private UserCreationRequest validRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validRequest = new UserCreationRequest();
        validRequest.setEmail("test@example.com");
        validRequest.setPassword("ValidPass123!");
        validRequest.setFirstName("John");
        validRequest.setLastName("Doe");
    }

    @Test
    void createUser_Success() {
        UserResponse response = new UserResponse();
        when(userService.createUser(any(UserCreationRequest.class))).thenReturn(response);

        UserResponse result = userServiceFacade.createUser(validRequest);

        assertNotNull(result);
        verify(userService).createUser(any(UserCreationRequest.class));
    }

    @Test
    void createUser_WhenUserServiceThrowsException_ThrowsAppException() {
        when(userService.createUser(any(UserCreationRequest.class)))
                .thenThrow(new AppException(ErrorCode.CREATE_USER_ERROR));

        assertThrows(AppException.class, () -> userServiceFacade.createUser(validRequest));
        verify(userService).createUser(any(UserCreationRequest.class));
    }
}
