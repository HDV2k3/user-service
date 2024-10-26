package com.user.identity.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.user.identity.dto.ApiResponse;
import com.user.identity.dto.request.UserCreationRequest;
import com.user.identity.dto.request.UserUpdateRequest;
import com.user.identity.dto.response.UserResponse;
import com.user.identity.facade.UserFacade;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "User Controller")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserFacade userFacade;

    @PostMapping("/create")
    @Operation(summary = "Create a new user")
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        var result = userFacade.createUser(request);
        return ApiResponse.success(result);
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {
        var result = userFacade.getUsers();
        return ApiResponse.success(result);
    }

    @GetMapping("/get-by-id/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable("userId") int userId) {
        var result = userFacade.getUser(userId);
        return ApiResponse.success(result);
    }

    @GetMapping("/my-info")
    ApiResponse<UserResponse> getMyInfo() {
        var result = userFacade.getMyInfo();
        return ApiResponse.success(result);
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable int userId) {
        var result = userFacade.deleteUser(userId);
        return ApiResponse.success(result);
    }

    @PutMapping("/update")
    ApiResponse<UserResponse> updateUser( @RequestBody UserUpdateRequest request) {
        var result = userFacade.updateUser( request);
        return ApiResponse.success(result);
    }

    @GetMapping("/me")
    ApiResponse<UserResponse> getMe() {
        var result = userFacade.getMe();
        return ApiResponse.success(result);
    }
}
