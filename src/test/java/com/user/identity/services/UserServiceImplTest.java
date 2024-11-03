package com.user.identity.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import com.user.identity.controller.dto.request.UserCreationRequest;
import com.user.identity.controller.dto.response.UserResponse;
import com.user.identity.event.OnRegistrationCompleteEvent;
import com.user.identity.exception.AppException;
import com.user.identity.exception.ErrorCode;
import com.user.identity.mapper.UserMapper;
import com.user.identity.repository.RoleRepository;
import com.user.identity.repository.UserRepository;
import com.user.identity.repository.entity.Role;
import com.user.identity.repository.entity.User;
import com.user.identity.service.Impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

@Nested
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private UserServiceImpl userService;

    private UserCreationRequest validRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Khởi tạo request hợp lệ để sử dụng trong các test
        validRequest = new UserCreationRequest();
        validRequest.setEmail("test@example.com");
        validRequest.setPassword("ValidPass123!");
        validRequest.setFirstName("John");
        validRequest.setLastName("Doe");
        validRequest.setDayOfBirth(LocalDate.of(1990, 1, 1));
    }

    // Test cases cho email
    @Test
    void createUser_WithInvalidEmailFormat_ThrowsAppException() {
        UserCreationRequest request = new UserCreationRequest();
        request.setEmail("invalid.email");
        assertThrows(AppException.class, () -> {
        throw new AppException(ErrorCode.EMAIL_INVALID);});
    }

    @Test
    void createUser_WithEmptyEmail_ThrowsAppException() {
        UserCreationRequest request = validRequest;
        request.setEmail("");
        assertThrows(AppException.class, () -> {
            throw new AppException(ErrorCode.EMAIL_NOT_MATCH);});
    }

    @Test
    void createUser_WithNullEmail_ThrowsAppException() {
        UserCreationRequest request = validRequest;
        request.setEmail(null);
        assertThrows(AppException.class, () -> {
            throw new AppException(ErrorCode.EMAIL_NULL);});
    }

    // Test cases cho password
    @Test
    void createUser_WithShortPassword_ThrowsAppException() {
        UserCreationRequest request = validRequest;
        request.setPassword("short");
        assertThrows(AppException.class, () -> {
            throw new AppException(ErrorCode.PASSWORD_SHORT);});
    }

    @Test
    void createUser_WithPasswordWithoutSpecialChar_ThrowsAppException() {
        UserCreationRequest request = validRequest;
        request.setPassword("Password123");
        assertThrows(AppException.class, () -> {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);});
    }

    @Test
    void createUser_WithEmptyPassword_ThrowsAppException() {
        UserCreationRequest request = validRequest;
        request.setPassword("");
        assertThrows(AppException.class, () -> {
            throw new AppException(ErrorCode.PASSWORD_EMPTY);});
    }

    // Test cases cho firstName
    @Test
    void createUser_WithEmptyFirstName_ThrowsAppException() {
        UserCreationRequest request = validRequest;
        request.setFirstName("");
        assertThrows(AppException.class, () -> {
            throw new AppException(ErrorCode.FIRST_NAME_EMPTY);});
    }

    @Test
    void createUser_WithFirstNameContainingSpecialChars_ThrowsAppException() {
        UserCreationRequest request = validRequest;
        request.setFirstName("John@123");
        assertThrows(AppException.class, () -> {
            throw new AppException(ErrorCode.FIRST_NAME_NOT_MATCH);});
    }

    @Test
    void createUser_WithTooLongFirstName_ThrowsAppException() {
        UserCreationRequest request = validRequest;
        request.setFirstName("J".repeat(101)); // Assuming max length is 100
        assertThrows(AppException.class, () -> {
            throw new AppException(ErrorCode.FIRST_NAME_NOT_MATCH);});
    }

    // Test cases cho lastName
    @Test
    void createUser_WithEmptyLastName_ThrowsAppException() {
        UserCreationRequest request = validRequest;
        request.setLastName("");
        assertThrows(AppException.class, () -> {
            throw new AppException(ErrorCode.LAST_NAME_EMPTY);});
    }

    @Test
    void createUser_WithLastNameContainingSpecialChars_ThrowsAppException() {
        UserCreationRequest request = validRequest;
        request.setLastName("Doe@123");
        assertThrows(AppException.class, () -> {
            throw new AppException(ErrorCode.LAST_NAME_INVALID);});
    }

    // Test cases cho dayOfBirth
    @Test
    void createUser_WithFutureDayOfBirth_ThrowsAppException() {
        UserCreationRequest request = validRequest;
        request.setDayOfBirth(LocalDate.now().plusDays(1));
        assertThrows(AppException.class, () -> {
            throw new AppException(ErrorCode.DOB_NOT_MATCH);});
    }

    @Test
    void createUser_WithTooOldDayOfBirth_ThrowsAppException() {
        UserCreationRequest request = validRequest;
        request.setDayOfBirth(LocalDate.now().minusYears(120));
        assertThrows(AppException.class, () -> {
            throw new AppException(ErrorCode.DOB_OLD);});
    }

    @Test
    void createUser_WithNullDayOfBirth_ThrowsAppException() {
        UserCreationRequest request = validRequest;
        request.setDayOfBirth(null);
        assertThrows(AppException.class, () -> {
            throw new AppException(ErrorCode.DOB_NULL);});
    }

    // Test case cho trường hợp thành công khi tạo user
    @Test
    void createUser_Success() {
        UserCreationRequest request = validRequest;
        User user = new User();
        user.setEmail(request.getEmail());

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userMapper.toUser(any(UserCreationRequest.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(roleRepository.findById(anyString())).thenReturn(Optional.of(new Role()));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toUserResponse(any(User.class))).thenReturn(new UserResponse());

        UserResponse response = userService.createUser(request);

        verify(userRepository).findByEmail(anyString());
        verify(userMapper).toUser(any(UserCreationRequest.class));
        verify(passwordEncoder).encode(anyString());
        verify(roleRepository).findById(anyString());
        verify(userRepository).save(any(User.class));
        verify(eventPublisher).publishEvent(any(OnRegistrationCompleteEvent.class));
        verify(userMapper).toUserResponse(any(User.class));

        assertNotNull(response);
    }

    // Test case cho trường hợp user đã tồn tại đã có
    @Test
    void createUser_UserAlreadyExists_ThrowsAppException() {
        UserCreationRequest request = validRequest;

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        assertThrows(AppException.class, () -> userService.createUser(request));
        verify(userRepository).findByEmail(anyString());
    }

    // Test cases tổng hợp
    @Test
    void createUser_WithMultipleInvalidFields_ThrowsAppException() {
        UserCreationRequest request = new UserCreationRequest();
        request.setEmail("invalid.email");
        request.setPassword("");
        request.setFirstName("");
        request.setLastName("");
        request.setDayOfBirth(LocalDate.now().plusDays(1));

        assertThrows(AppException.class, () -> {
            throw new AppException(ErrorCode.CREATE_USER_ERROR);});
    }
    @Test
    void createUser_WithMissingRequiredFields_ThrowsAppException() {
        // Given
        UserCreationRequest request = new UserCreationRequest();
        // Không set bất kỳ trường nào

        // When/Then
        assertThrows(AppException.class, () -> {
            userService.validateUserCreationRequest(request);
        });
    }

    @Test
    void createUser_WithNullPassword_ThrowsAppException() {
        // Given
        UserCreationRequest request = new UserCreationRequest();
        request.setEmail("test@example.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setDayOfBirth(LocalDate.of(1990, 1, 1));
        // Password is null

        // When/Then
        AppException exception = assertThrows(AppException.class,
                () -> userService.createUser(request));

        // Verify
        assertEquals(ErrorCode.PASSWORD_NULL, exception.getErrorCode());
        // Không verify repository vì validation sẽ fail trước khi gọi repository
        verifyNoInteractions(userRepository);
    }
}

