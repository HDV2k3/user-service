package com.user.identity.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.user.identity.dto.ApiResponse;
import com.user.identity.dto.request.RoleRequest;
import com.user.identity.dto.response.RoleResponse;
import com.user.identity.facade.RoleFacade;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Role Controller")
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {
    RoleFacade roleFacade;

    @PostMapping
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        var result = roleFacade.create(request);
        return ApiResponse.success(result);
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAll() {
        var result = roleFacade.getAll();
        return ApiResponse.success(result);
    }

    @DeleteMapping("/{role}")
    ApiResponse<String> delete(@PathVariable String role) {
        var result = roleFacade.delete(role);
        return ApiResponse.success(result);
    }
}
