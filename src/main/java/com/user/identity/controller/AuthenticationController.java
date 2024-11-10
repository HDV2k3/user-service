package com.user.identity.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.identity.controller.dto.ApiResponse;
import com.user.identity.controller.dto.request.AuthenticationRequest;
import com.user.identity.controller.dto.request.IntrospectRequest;
import com.user.identity.controller.dto.request.LogoutRequest;
import com.user.identity.controller.dto.request.RefreshRequest;
import com.user.identity.controller.dto.response.AuthenticationResponse;
import com.user.identity.controller.dto.response.IntrospectResponse;
import com.user.identity.facade.AuthenticationFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Authentication Controller", description = "Các endpoint cho xác thực người dùng và quản lý token")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationController {
    AuthenticationFacade authenticationFacade;

    /**
     * Xác thực người dùng và tạo token.
     *
     * @param request yêu cầu xác thực chứa thông tin người dùng
     * @return phản hồi thành công chứa token xác thực
     */
    @PostMapping("/login")
    @Operation(summary = "Đăng nhập người dùng",
            description = "Xác thực người dùng và trả về token JWT."
    )
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationFacade.authenticate(request);
        return ApiResponse.success(result);
    }

    /**
     * Kiểm tra tính hợp lệ của token.
     *
     * @param request yêu cầu introspect chứa token
     * @return phản hồi cho biết token có hợp lệ hay không
     */
    @PostMapping("/introspect")
    @Operation(summary = "Kiểm tra token",
            description = "Kiểm tra xem token có hợp lệ hay không."
    )
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws Exception {
        var result = authenticationFacade.introspect(request);
        return ApiResponse.success(result);
    }

    /**
     * Làm mới token xác thực.
     *
     * @param request yêu cầu làm mới token chứa token cũ
     * @return phản hồi mới với token mới
     */
    @PostMapping("/refresh")
    @Operation(summary = "Làm mới token",
            description = "Tạo một token mới từ refresh token."
    )
    public ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshRequest request) throws Exception {
        var result = authenticationFacade.refreshToken(request);
        return ApiResponse.success(result);
    }

    /**
     * Đăng xuất người dùng và hủy bỏ token.
     *
     * @param request yêu cầu đăng xuất chứa token cần hủy bỏ
     * @return phản hồi cho biết kết quả của thao tác đăng xuất
     */
    @PostMapping("/logout")
    @Operation(summary = "Đăng xuất người dùng",
            description = "Hủy bỏ token của người dùng để đăng xuất."
    )
    public ApiResponse<String> logout(@RequestBody LogoutRequest request) throws Exception {
        var result = authenticationFacade.logout(request);
        return ApiResponse.success(result);
    }
}
