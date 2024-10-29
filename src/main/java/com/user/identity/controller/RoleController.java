package com.user.identity.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.user.identity.controller.dto.ApiResponse;
import com.user.identity.controller.dto.request.RoleRequest;
import com.user.identity.controller.dto.response.RoleResponse;
import com.user.identity.facade.RoleFacade;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Role Controller", description = "API for managing user roles")
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {

    RoleFacade roleFacade;

    @Operation(summary = "Create a new role", description = "Creates a new role in the system.")
    @PostMapping
    public ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        var result = roleFacade.create(request);
        return ApiResponse.success(result);
    }

    @Operation(summary = "Retrieve all roles", description = "Fetches a list of all available roles.")
    @GetMapping
    public ApiResponse<List<RoleResponse>> getAll() {
        var result = roleFacade.getAll();
        return ApiResponse.success(result);
    }

    @Operation(summary = "Delete a role", description = "Deletes a specific role from the system by name.")
    @DeleteMapping("/{role}")
    public ApiResponse<String> delete(@PathVariable String role) {
        var result = roleFacade.delete(role);
        return ApiResponse.success(result);
    }
}
