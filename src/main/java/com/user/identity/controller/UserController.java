package com.user.identity.controller;

import com.user.identity.controller.dto.ApiResponse;
import com.user.identity.controller.dto.request.SupportRequest;
import com.user.identity.controller.dto.request.UserCreationRequest;
import com.user.identity.controller.dto.request.UserUpdateRequest;
import com.user.identity.controller.dto.response.InfoUserForCount;
import com.user.identity.controller.dto.response.UserResponse;
import com.user.identity.facade.UserFacade;
import com.user.identity.facade.VerifyFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Tag(name = "User Controller", description = "Quản lý các hoạt động người dùng như tạo mới, cập nhật, xóa, xác minh, v.v.")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {

    UserFacade userFacade;
    VerifyFacade verifyFacade;

    /**
     * Tạo một người dùng mới.
     *
     * @param request thông tin người dùng cần tạo
     * @return phản hồi thành công với thông tin người dùng vừa tạo
     */
    @Operation(summary = "Tạo người dùng mới", description = "Đăng ký một người dùng mới với thông tin đã cung cấp.")
    @PostMapping("/create")
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        var result = userFacade.createUser(request);
        return ApiResponse.success(result);
    }

    /**
     * Lấy tất cả người dùng.
     *
     * @return danh sách tất cả người dùng đã đăng ký
     */
    @Operation(summary = "Lấy tất cả người dùng", description = "Lấy danh sách tất cả người dùng đã đăng ký trong hệ thống.", security = {@SecurityRequirement(name = "bearerAuth")})
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN

    public ApiResponse<List<UserResponse>> getUsers() {
        var result = userFacade.getUsers();
        return ApiResponse.success(result);
    }

    /**
     * Lấy thông tin người dùng theo ID.
     *
     * @param userId ID của người dùng
     * @return thông tin của người dùng theo ID
     */
    @Operation(summary = "Lấy thông tin người dùng theo ID", description = "Lấy thông tin của một người dùng cụ thể theo ID của họ.", security = {@SecurityRequirement(name = "bearerAuth")})
    @GetMapping("/get-by-id/{userId}")
    public ApiResponse<UserResponse> getUser(@PathVariable("userId") int userId) {
        var result = userFacade.getUser(userId);
        return ApiResponse.success(result);
    }

    /**
     * Lấy thông tin người dùng hiện tại.
     *
     * @return thông tin của người dùng hiện tại (người dùng đã xác thực)
     */
    @Operation(summary = "Lấy thông tin người dùng hiện tại", description = "Lấy thông tin của người dùng hiện tại đã đăng nhập.", security = {@SecurityRequirement(name = "bearerAuth")})
    @GetMapping("/my-info")
    public ApiResponse<UserResponse> getMyInfo() {
        var result = userFacade.getMyInfo();
        return ApiResponse.success(result);
    }

    /**
     * Xóa người dùng theo ID.
     *
     * @param userId ID của người dùng cần xóa
     * @return phản hồi thành công khi xóa người dùng
     */
    @Operation(summary = "Xóa người dùng theo ID", description = "Xóa người dùng với ID đã cung cấp.", security = {@SecurityRequirement(name = "bearerAuth")})
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Yêu cầu Bearer token với quyền ADMIN
    public ApiResponse<String> deleteUser(@PathVariable int userId) {
        var result = userFacade.deleteUser(userId);
        return ApiResponse.success(result);
    }

    /**
     * Cập nhật thông tin người dùng.
     *
     * @param request thông tin người dùng cần cập nhật
     * @return phản hồi thành công với thông tin người dùng sau khi cập nhật
     */
    @Operation(summary = "Cập nhật thông tin người dùng", description = "Cập nhật thông tin người dùng với các chi tiết đã cung cấp.", security = {@SecurityRequirement(name = "bearerAuth")})
    @PutMapping("/update")
    public ApiResponse<UserResponse> updateUser(@RequestBody @Valid UserUpdateRequest request) {
        var result = userFacade.updateUser(request);
        return ApiResponse.success(result);
    }

    /**
     * Lấy thông tin người dùng hiện tại đã xác thực.
     *
     * @return thông tin của người dùng đã xác thực hiện tại
     */
    @Operation(summary = "Lấy thông tin người dùng hiện tại đã xác thực", description = "Lấy thông tin của người dùng hiện tại đã đăng nhập và xác thực.")
    @GetMapping("/me")
    public ApiResponse<UserResponse> getMe() {
        var result = userFacade.getMe();
        return ApiResponse.success(result);
    }

    /**
     * Xác minh email người dùng.
     *
     * @param token mã thông báo để xác minh email
     * @return phản hồi xác nhận xác minh email thành công
     */
    @Operation(summary = "Xác minh email người dùng", description = "Xác minh email của người dùng sử dụng mã token đã cung cấp.")
    @GetMapping("/verify-email")
    public ApiResponse<Map<String, Object>> verifyEmail(@RequestParam("token") String token) {
        var result = verifyFacade.verifyEmail(token);
        return ApiResponse.success(result);
    }

    /**
     * Gửi lại liên kết xác minh email.
     *
     * @param email mã thông báo để gửi lại liên kết xác minh
     * @return phản hồi xác nhận việc gửi lại liên kết xác minh thành công
     */
    @Operation(summary = "Gửi lại liên kết xác minh email", description = "Gửi lại liên kết xác minh email cho người dùng với token đã cung cấp.")
    @PostMapping("/resend-verification")
    public ApiResponse<String> resendVerification(@RequestParam("email") String email) {
        var result = verifyFacade.resendVerification(email);
        return ApiResponse.success(result);
    }

    /**
     * Tải lên hình ảnh cho bài viết phòng cho thuê hoặc bán.
     *
     * @param userId ID của bài viết cần tải hình ảnh.
     * @param file   Các tệp hình ảnh cần tải lên.
     * @return Danh sách các hình ảnh đã tải lên cho bài viết.
     */
    @Operation(summary = "Upload avatar", description = "Upload avatar.", security = {@SecurityRequirement(name = "bearerAuth")})
    @PostMapping(value = "/upload-avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> uploadPostImages(@RequestParam int userId, @RequestPart(value = "file", required = false) MultipartFile file) {
        String uploadedImages = userFacade.uploadAvatar(userId, file);
        return ApiResponse.success(uploadedImages);
    }

    @GetMapping("/quantityUser")
    public ApiResponse<InfoUserForCount> countUser() {
        return ApiResponse.success(userFacade.countUser());
    }

    @PostMapping("/support")
    public ApiResponse<String> supportEmail(@RequestBody SupportRequest request) {
        return ApiResponse.success(userFacade.supportEmail(request));
    }
}
