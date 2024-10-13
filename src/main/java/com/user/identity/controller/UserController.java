package com.user.identity.controller;


import com.user.identity.dto.ApiResponse;
import com.user.identity.dto.request.UserCreationRequest;
import com.user.identity.dto.request.UserUpdateRequest;
import com.user.identity.dto.response.UserResponse;
import com.user.identity.facade.UserFacade;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "User Controller")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserFacade userFacade;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
    var result = userFacade.createUser(request);
    return ApiResponse.success(result);
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {
        var result = userFacade.getUsers();
        return ApiResponse.success(result);
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
        var result = userFacade.getUser(userId);
        return ApiResponse.success(result);
    }

    @GetMapping("/my-info")
    ApiResponse<UserResponse> getMyInfo() {
        var result = userFacade.getMyInfo();
        return ApiResponse.success(result);
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        var result = userFacade.deleteUser(userId);
        return ApiResponse.success(result);
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        var result = userFacade.updateUser(userId,request);
        return ApiResponse.success(result);
    }
}
