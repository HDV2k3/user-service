package com.user.identity.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
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

@Tag(name = "Role Controller", description = "API quản lý vai trò người dùng")
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {

    RoleFacade roleFacade;

    /**
     * Tạo một vai trò mới.
     *
     * @param request thông tin vai trò cần tạo
     * @return phản hồi thành công với thông tin vai trò vừa tạo
     */
    @Operation(
            summary = "Tạo vai trò mới",
            description = "API này cho phép tạo một vai trò mới trong hệ thống."
            ,security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    @PostMapping
    public ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        var result = roleFacade.create(request);
        return ApiResponse.success(result);
    }

    /**
     * Lấy danh sách tất cả các vai trò.
     *
     * @return danh sách tất cả các vai trò trong hệ thống
     */
    @Operation(
            summary = "Lấy tất cả vai trò",
            description = "API này trả về danh sách tất cả các vai trò có sẵn trong hệ thống."
            ,security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    @GetMapping
    public ApiResponse<List<RoleResponse>> getAll() {
        var result = roleFacade.getAll();
        return ApiResponse.success(result);
    }

    /**
     * Xóa vai trò theo tên.
     *
     * @param role tên của vai trò cần xóa
     * @return phản hồi kết quả xóa vai trò
     */
    @Operation(
            summary = "Xóa vai trò",
            description = "API này cho phép xóa vai trò dựa trên tên vai trò."
            ,security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    @DeleteMapping("/{role}")
    public ApiResponse<String> delete(@PathVariable String role) {
        var result = roleFacade.delete(role);
        return ApiResponse.success(result);
    }
}
