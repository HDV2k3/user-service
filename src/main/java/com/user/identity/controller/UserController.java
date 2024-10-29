package com.user.identity.controller;

import com.user.identity.controller.dto.ApiResponse;
import com.user.identity.controller.dto.request.UserCreationRequest;
import com.user.identity.controller.dto.request.UserUpdateRequest;
import com.user.identity.controller.dto.response.UserResponse;
import com.user.identity.facade.UserFacade;
import com.user.identity.facade.VerifyFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "User Controller", description = "Manage user operations such as creation, update, deletion, verification, etc.")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {

    UserFacade userFacade;
    VerifyFacade verifyFacade;

    @Operation(summary = "Create a new user", description = "Registers a new user with the provided details.")
    @PostMapping("/create")
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        var result = userFacade.createUser(request);
        return ApiResponse.success(result);
    }

    @Operation(summary = "Retrieve all users", description = "Fetches a list of all registered users.")
    @GetMapping
    public ApiResponse<List<UserResponse>> getUsers() {
        var result = userFacade.getUsers();
        return ApiResponse.success(result);
    }

    @Operation(summary = "Retrieve user by ID", description = "Fetches details of a specific user by their unique ID.")
    @GetMapping("/get-by-id/{userId}")
    public ApiResponse<UserResponse> getUser(@PathVariable("userId") int userId) {
        var result = userFacade.getUser(userId);
        return ApiResponse.success(result);
    }

    @Operation(summary = "Retrieve current user information", description = "Fetches information of the currently authenticated user.")
    @GetMapping("/my-info")
    public ApiResponse<UserResponse> getMyInfo() {
        var result = userFacade.getMyInfo();
        return ApiResponse.success(result);
    }

    @Operation(summary = "Delete user by ID", description = "Deletes a user with the provided unique ID.")
    @DeleteMapping("/{userId}")
    public ApiResponse<String> deleteUser(@PathVariable int userId) {
        var result = userFacade.deleteUser(userId);
        return ApiResponse.success(result);
    }

    @Operation(summary = "Update user details", description = "Updates the user information with the provided details.")
    @PutMapping("/update")
    public ApiResponse<UserResponse> updateUser(@RequestBody @Valid UserUpdateRequest request) {
        var result = userFacade.updateUser(request);
        return ApiResponse.success(result);
    }

    @Operation(summary = "Retrieve current authenticated user", description = "Fetches the details of the user who is currently authenticated.")
    @GetMapping("/me")
    public ApiResponse<UserResponse> getMe() {
        var result = userFacade.getMe();
        return ApiResponse.success(result);
    }

    @Operation(summary = "Verify user email", description = "Verifies the user's email using the provided token.")
    @GetMapping("/verify-email")
    public ApiResponse<Map<String, Object>> verifyEmail(@RequestParam("token") String token) {
        var result = verifyFacade.verifyEmail(token);
        return ApiResponse.success(result);
    }

    @Operation(summary = "Resend email verification", description = "Resends the email verification link to the user associated with the given token.")
    @PostMapping("/resend-verification")
    public ApiResponse<String> resendVerification(@RequestParam("token") String token) {
        var result = verifyFacade.resendVerification(token);
        return ApiResponse.success(result);
    }
}
