package com.user.identity.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
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

@Tag(name = "Permission Controller", description = "API quản lý quyền người dùng")
@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionController {

    PermissionFacade permissionFacade;

    /**
     * Tạo một quyền mới.
     *
     * @param request thông tin quyền cần tạo
     * @return phản hồi thành công với thông tin quyền vừa được tạo
     */
    @Operation(
            summary = "Tạo quyền mới",
            description = "API này cho phép tạo quyền mới trong hệ thống."
            ,security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    @PostMapping
    public ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
        var result = permissionFacade.create(request);
        return ApiResponse.success(result);
    }

    /**
     * Lấy danh sách tất cả các quyền.
     *
     * @return danh sách tất cả các quyền trong hệ thống
     */
    @Operation(
            summary = "Lấy tất cả quyền",
            description = "API này trả về danh sách tất cả quyền có sẵn trong hệ thống."
            ,security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    @GetMapping
    public ApiResponse<List<PermissionResponse>> getAll() {
        var result = permissionFacade.getAll();
        return ApiResponse.success(result);
    }

    /**
     * Xóa quyền theo ID.
     *
     * @param permission ID của quyền cần xóa
     * @return phản hồi thông báo kết quả xóa quyền
     */
    @Operation(
            summary = "Xóa quyền",
            description = "API này cho phép xóa quyền dựa trên ID của quyền cần xóa."
            ,security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    @DeleteMapping("/{permission}")
    public ApiResponse<String> delete(@PathVariable String permission) {
        var result = permissionFacade.delete(permission);
        return ApiResponse.success(result);
    }
}
