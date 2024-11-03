package com.user.identity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.identity.controller.dto.request.UserCreationRequest;
import com.user.identity.controller.dto.response.UserResponse;
import com.user.identity.facade.UserFacade;
import com.user.identity.exception.AppException;
import com.user.identity.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserFacade userServiceFacade;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private UserCreationRequest validRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();

        validRequest = new UserCreationRequest();
        validRequest.setEmail("test@example.com");
        validRequest.setPassword("ValidPass123!");
        validRequest.setFirstName("John");
        validRequest.setLastName("Doe");
    }

    @Test
    void createUser_ValidRequest_ReturnsUserResponse() throws Exception {
        UserResponse response = new UserResponse();
        when(userServiceFacade.createUser(any(UserCreationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").exists());
    }

    @Test
    void createUser_InvalidRequest_ThrowsAppException() throws Exception {
        when(userServiceFacade.createUser(any(UserCreationRequest.class)))
                .thenThrow(new AppException(ErrorCode.CREATE_USER_ERROR));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }
}
