package com.user.identity.service;

import java.util.List;

import com.user.identity.dto.request.UserCreationRequest;
import com.user.identity.dto.request.UserUpdateRequest;
import com.user.identity.dto.response.UserResponse;

public interface UserService {
    UserResponse createUser(UserCreationRequest request);

    UserResponse getMyInfo();

    UserResponse updateUser( UserUpdateRequest request);

    String deleteUser(int userId);

    List<UserResponse> getUsers();

    UserResponse getUser(int id);

    UserResponse getMe();
}
