package com.user.identity.service;

import java.util.List;

import com.user.identity.controller.dto.request.UserCreationRequest;
import com.user.identity.controller.dto.request.UserUpdateRequest;
import com.user.identity.controller.dto.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserResponse createUser(UserCreationRequest request);

    UserResponse getMyInfo();

    UserResponse updateUser( UserUpdateRequest request);

    String deleteUser(int userId);

    List<UserResponse> getUsers();

    UserResponse getUser(int id);

    UserResponse getMe();
    String  uploadImagesAvatar(int userId, MultipartFile file);
}
