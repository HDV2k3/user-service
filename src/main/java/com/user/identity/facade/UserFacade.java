package com.user.identity.facade;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.user.identity.controller.dto.request.UserCreationRequest;
import com.user.identity.controller.dto.request.UserUpdateRequest;
import com.user.identity.controller.dto.response.UserResponse;
import com.user.identity.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserFacade {
    UserService userService;
    /**
     * Creates a new user based on the provided request.
     *
     * @param request the user creation request containing details for the new user
     * @return UserResponse containing the created user details
     */
    public UserResponse createUser(UserCreationRequest request) {
        return userService.createUser(request);
    }

    /**
     * Retrieves the details of the currently authenticated user.
     *
     * @return UserResponse containing the details of the current user
     */
    public UserResponse getMyInfo() {
        return userService.getMyInfo();
    }

    /**
     * Updates the details of a specific user identified by their ID.
     *
     * @param request the user update request containing updated user details
     * @return UserResponse containing the updated user details
     */
    public UserResponse updateUser( UserUpdateRequest request) {
        return userService.updateUser(request);
    }

    /**
     * Deletes a specific user identified by their ID.
     *
     * @param userId the ID of the user to delete
     */
    public String deleteUser(int userId) {
        userService.deleteUser(userId);
        return "Delete Successfully";
    }

    /**
     * Retrieves a list of all users in the system.
     *
     * @return a list of UserResponse objects representing all users
     */
    public List<UserResponse> getUsers() {
        return userService.getUsers();
    }

    /**
     * Retrieves details of a specific user identified by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return UserResponse containing the user details
     */
    public UserResponse getUser(int id) {
        return userService.getUser(id);
    }

    public UserResponse getMe() {
        return userService.getMe();
    }



}
