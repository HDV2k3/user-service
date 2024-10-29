package com.user.identity.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.user.identity.controller.dto.ApiResponse;
import com.user.identity.controller.dto.request.PermissionRequest;
import com.user.identity.controller.dto.response.PermissionResponse;
import com.user.identity.facade.PermissionFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Permission Controller", description = "API for managing user permissions")
@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionController {

    PermissionFacade permissionFacade;

    @Operation(
            summary = "Create a new permission",
            description = "This endpoint allows for creating a new permission in the system."
    )
    @PostMapping
    public ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
        var result = permissionFacade.create(request);
        return ApiResponse.success(result);
    }

    @Operation(
            summary = "Retrieve all permissions",
            description = "Fetches a list of all permissions available in the system."
    )
    @GetMapping
    public ApiResponse<List<PermissionResponse>> getAll() {
        var result = permissionFacade.getAll();
        return ApiResponse.success(result);
    }

    @Operation(
            summary = "Delete a permission",
            description = "Deletes a specific permission based on its identifier."
    )
    @DeleteMapping("/{permission}")
    public ApiResponse<String> delete(@PathVariable String permission) {
        var result = permissionFacade.delete(permission);
        return ApiResponse.success(result);
    }
}
